package com.jdbc.student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StudentDatabase
 {  
	 private static Connection connection=null;
	 private static Scanner scanner=new Scanner(System.in);
	public static void main(String[] args)  
	{ StudentDatabase studentDatabase=new StudentDatabase();
	    try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			 connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcdb", "root", "puw544");
			 System.out.println("ENTER CHOICE");
			 System.out.println("1-Insertion\n2-Selection\n3-Select all record\n4-update record\n5-Delete record\n6-Transaction");
			 int choice=Integer.parseInt(scanner.nextLine());
			 switch (choice) {
			case 1: studentDatabase.insertrecord();break;
			case 2: studentDatabase.selectrecord();break;
			case 3: studentDatabase.selectallrecord();break;
			case 4:studentDatabase.updaterecord();break;
			case 5:studentDatabase.deleterecord();break;
			case 6:studentDatabase.transaction();break;
			
			default:throw new IllegalArgumentException("wrong choice: " + choice);
			}
		} catch (ClassNotFoundException | SQLException e) {
			
			System.out.println("something wrong happened");
		}
	} 
	private void insertrecord() throws SQLException
	{   String sqlString="insert into student(name,percentage,city) values(?,?,?)";
		 PreparedStatement preparedStatement=connection.prepareStatement(sqlString);
		  System.out.println("enter name:-  ");
		  preparedStatement.setString(1,scanner.nextLine());
		  System.out.println("entr percentage:- ");
		  preparedStatement.setDouble(2,Double.parseDouble(scanner.nextLine()));
		  System.out.println("enetr city:- ");
		  preparedStatement.setString(3,scanner.nextLine());
		 int row= preparedStatement.executeUpdate();
		 if(row>1)System.out.println("record inserted succesfully");
		  
	}
	public void selectrecord() throws SQLException
	{
		 Statement statement=connection.createStatement();
		 System.out.println("enetr roll number to find detail");
		 int number=Integer.parseInt(scanner.nextLine());
		 String sqlString="select* from student where roll_number="+number;
			ResultSet resultSet	 =statement.executeQuery(sqlString);
		 if(resultSet.next())
		 {
			 System.out.println("Roll number:-"+resultSet.getInt(1)+" "+resultSet.getString(2)+" "+resultSet.getDouble(3)+" "+resultSet.getString(4));
		 }
		 else {
			 System.out.println("no such roll number exist");
		 }
	}
	public void selectallrecord() throws SQLException
	{
		CallableStatement callableStatement=connection.prepareCall("{call getall()}");
		ResultSet resultSet=callableStatement.executeQuery();
		while(resultSet.next())
		{
			System.out.println("roll number is:\t"+resultSet.getInt("roll_number"));
			System.out.println("Name is:\t"+resultSet.getString("name"));
			System.out.println("percentage is:\t"+resultSet.getDouble("percentage"));
			System.out.println("city is:\t"+resultSet.getString("city"));
			System.out.println("-----------------------------------");
		}
	}
	public void updaterecord() throws SQLException
	{
		Statement statement=connection.createStatement();
		System.out.println("enter roll number to update data");
		int number=Integer.parseInt(scanner.nextLine());
		String string="select * from student where roll_number= "+number;
		ResultSet resultSet=statement.executeQuery(string);
		 if(resultSet.next())
		 {
			
				 int roll_number=resultSet.getInt("roll_number");
				 String name=resultSet.getString("name");
				 double percentage=resultSet.getDouble("percentage");
				 String city=resultSet.getString("city");
				 System.out.println(roll_number+" |"+name+" |"+percentage+" |"+city);
				 System.out.println("what do u want to update?");
				 System.out.println("1-Name\n2-percentege\n3-city");
				 int choice=Integer.parseInt(scanner.nextLine());
				 String sqString="update student set";
				switch (choice) {
				case 1:  System.out.println("enter new name");
				 String string2=scanner.nextLine();
				 sqString=sqString+"name=? where roll_number="+roll_number;
				 PreparedStatement preparedStatement=connection.prepareStatement(sqString);
				 preparedStatement.setString(1, string2);
				 preparedStatement.executeUpdate();break;
				case 2: System.out.println("enetr new percentage");
				 double a=Double.parseDouble(scanner.nextLine());
				 sqString=sqString+"percentage=? where roll_number="+roll_number;
				 PreparedStatement preparedStatement1=connection.prepareStatement(sqString);
				 preparedStatement1.setDouble(1, a);
				 preparedStatement1.executeUpdate();break;
				case 3: System.out.println("enter new city");
				 String string23=scanner.nextLine();
				 sqString=sqString+"city=? where roll_number="+roll_number;
				 PreparedStatement preparedStatement3=connection.prepareStatement(sqString);
				 preparedStatement3.setString(1, string23);
				 preparedStatement3.executeUpdate();
				      
				default:
					throw new IllegalArgumentException("Unexpected value: " + choice);
				}
				 
			  
		 }
		 else {
			 System.out.println("no such roll number exist");
		 }
	}
	public void deleterecord() throws SQLException
	{
//		Statement statement=connection.createStatement();
		
		String string="Delete from student where roll_number=";
		System.out.println("enetr roll number to delete");
		int choice=Integer.parseInt(scanner.nextLine());
		String sqlString=string+""+choice;
//		int row=statement.executeUpdate(sqlString);
		PreparedStatement preparedStatement=connection.prepareStatement(sqlString);
		int row=preparedStatement.executeUpdate();
		if(row>0)System.out.println("record deleted");
		else System.out.println("record not deleted");
	}
	public void transaction() throws SQLException
	{
		String sql1="insert into student (name,percentage,city)values('vik',45,'chennai')";
		String sql2="insert into student (name,percentage,city)values('viki',85,'delhi')";
		connection.setAutoCommit(false);
		   PreparedStatement preparedStatement=connection.prepareStatement(sql1);
		   int row1=preparedStatement.executeUpdate();
		   PreparedStatement preparedStatement1=connection.prepareStatement(sql2);
		   int row2=preparedStatement1.executeUpdate();
		   if(row2>0&&row1>0) {connection.commit();System.out.println("record inserted");}
	}
	
 }
