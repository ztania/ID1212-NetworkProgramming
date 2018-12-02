/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.integration;

/**
 *
 * @author Tania
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import common.Credentials;
import server.model.FileObject;
import server.model.User;

//class for accessing the database and handling all the database calls
public class DatabaseAccess {
	
	private FileObject file;
	private Boolean credentialCheck;
	
	private PreparedStatement registerStmt;
	private PreparedStatement unregisterStmt;
	private PreparedStatement uploadFileStmt;
	private PreparedStatement deleteFileStmt;
	private PreparedStatement updateFileStmt;
	private PreparedStatement downloadFileStmt;
	private PreparedStatement listFilesStmt;
	private PreparedStatement checkLoginStatement;
	private PreparedStatement findUserStmt;
	
	
	// creating JDBC connecting to the database
	public DatabaseAccess() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/id1212", "root", "root");
			prepareStatements(connection);
		} 
		catch (SQLException | ClassNotFoundException e) {	
			e.printStackTrace();
		}	
	}	
	
	//registering a user in the system
	public void register(User user) throws DatabaseAccessException{
		String failureMsg = "Could not create the account. Username is already taken.";
		try {
			registerStmt.setString(1, user.getUsername());
			registerStmt.setString(2, user.getPassword());
			int rows = registerStmt.executeUpdate();
			
            if (rows != 1) {
                throw new DatabaseAccessException(failureMsg);
            }
        } catch (SQLException ex) {
            throw new DatabaseAccessException(failureMsg, ex);
        }
	}
	
	//unregister or delete a user from the system
	public void unregister(Credentials credentials) {	
		
		try {
			unregisterStmt.setString(1, credentials.getUsername());
			unregisterStmt.setString(2, credentials.getPassword());
			unregisterStmt.executeUpdate();
		} 
		catch (SQLException e) {
			
			System.out.println("Could not unregister user");
			e.printStackTrace();
		}
	}
	// searching for user account by username
	public User findAccountByName(String name) throws DatabaseAccessException {
        ResultSet result = null;
        try {
            findUserStmt.setString(1, name);
            result = findUserStmt.executeQuery();
            if (result.next()) {
                String userName = result.getString("username");
                String password = result.getString("password");
                return new User(userName, password);
            }
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Can't find account by username: " + name, ex);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
	//upload or add a file to the database catalog
	public void uploadFile(FileObject file) {
		
		try {
			uploadFileStmt.setString(1, file.getName());
			uploadFileStmt.setInt(2, file.getSize());
			uploadFileStmt.setString(3, file.getOwner());
			uploadFileStmt.setString(4, file.getAccess());
			uploadFileStmt.setString(5, file.getPermission());
			uploadFileStmt.setString(6, file.getNotification());
			uploadFileStmt.executeUpdate();
		} 
		catch (SQLException e) {
			System.out.println("Could not upload file");
			e.printStackTrace();
		}
	}
	
        //update file metadata i.e. name, size,access
	public void updateFile(FileObject file) {
		
		try {
			
			updateFileStmt.setInt(1, file.getSize());
			updateFileStmt.setString(2, file.getOwner());
			updateFileStmt.setString(3, file.getAccess());
			updateFileStmt.setString(4, file.getPermission());
			updateFileStmt.setString(5, file.getNotification());
			updateFileStmt.setString(6, file.getName());
			
			updateFileStmt.executeUpdate();
		} 
		catch (SQLException e) {
			
			System.out.println("Could not update file");
			e.printStackTrace();
		}
	}
	
	//download/access a file from the database
	public FileObject downloadFile(String fileName) {
		
		try {
			
			downloadFileStmt.setString(1, fileName);
			ResultSet result = downloadFileStmt.executeQuery();
			
			while (result.next()) {
				
				file = new FileObject(result.getString(1), result.getInt(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(file.toString());
		
		return file;
		
	}
	
	//delete a file, remove from the database
	public void deleteFile(String fileName) {
		
		try {
			deleteFileStmt.setString(1, fileName);
			deleteFileStmt.executeUpdate();
		} 
		catch (SQLException e) {
			System.out.println("File could not be deleted");
			e.printStackTrace();
		}	
	}
	//listing all the database from the table
	public List<FileObject> listFiles() {
		
		List<FileObject> files = new ArrayList<>();
		
		try {
			
			ResultSet result = listFilesStmt.executeQuery();
			while (result.next()) {
				
				file = new FileObject(result.getString(1), result.getInt(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6));
				files.add(file);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return files;
	}
	
	//download a file from the database
	public Boolean checkLogin(Credentials credentials) {
		
		try {
			checkLoginStatement.setString(1, credentials.getUsername());
			checkLoginStatement.setString(2, credentials.getPassword());
			ResultSet result = checkLoginStatement.executeQuery();
			
			if (result.next()) {
				
				credentialCheck = true;
			}
			else {
				
				credentialCheck = false;	
				}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return credentialCheck;	
	}
	
	
	public void prepareStatements(Connection connection) throws SQLException {
		
		registerStmt = connection.prepareStatement("insert into user values(?, ?)");
		unregisterStmt = connection.prepareStatement("delete from user where username = ? and password = ?");
		uploadFileStmt = connection.prepareStatement("insert into file values(?, ?, ?, ?, ?, ?)");
		deleteFileStmt = connection.prepareStatement("delete from file where fileName = ?");
		downloadFileStmt = connection.prepareStatement("select * from file where fileName = ?");
		listFilesStmt = connection.prepareStatement("select * from file");
		checkLoginStatement = connection.prepareStatement("select * from user where username = ? and password = ?");
		findUserStmt = connection.prepareStatement("select * from user where username = ?");
	}
}

