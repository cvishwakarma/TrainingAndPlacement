/*
 * ProfileDAO.java
 *
 * 
 * 
 */

package com.dts.dae.dao;

import com.dts.dae.model.Profile;
import com.dts.core.dao.AbstractDataAccessObject; 
import com.dts.core.util.DateWrapper;
import com.dts.core.util.LoggerManager;
import com.dts.dae.dto.RegistrationBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


/**
 *
 * @author 
 */
public class ProfileDAO extends AbstractDataAccessObject{
    
    public Connection con;

    private boolean flag;
    /** Creates a new instance of ProfileDAO */
    public ProfileDAO() 
    {
           //getting Database Connection
           
    }
    
       
    //User Registration
    public boolean registration(Profile regbean)
    {
        String loginid=regbean.getLoginID();
        String password=regbean.getPassword();
        String firstname=regbean.getFirstName();
        String lastname=regbean.getLastName();
        String logintype=regbean.getLoginType();
        int status=regbean.getStatus();
        //String regdate=regbean.getRegDate();
        String secretquest=regbean.getSecretQuestionID();
        String ownsecretquest=regbean.getOwnSecretQuestion();
        String secretans=regbean.getSecretAnswer();
        int firstlogin=regbean.getFirstLogin();
        String bdate=DateWrapper.parseDate(regbean.getBirthDate());
        String hno = regbean.getHno();
        String street = regbean.getStreet();
        String city=regbean.getCity();
        String state=regbean.getState();
        String country=regbean.getCountry();
        String pincode = regbean.getPincode();
        String phoneno = regbean.getPhoneNo();
        String email = regbean.getEmail();
        String locale=regbean.getLocale();
        String passmdate=regbean.getPasswordModifiedDate();
        String profilemdate=regbean.getProfileModifiedDate();
        
        try 
        {
        	con=getConnection();
           con.setAutoCommit(false);
           PreparedStatement pst=null;
           Statement st=con.createStatement();
           int i=0;
           if(secretquest=="")
           {
                ResultSet rs=st.executeQuery("select (max(questionid))+1 from questionbase");
                if(rs.next())
                    secretquest=rs.getString(1);
                pst = con.prepareStatement("INSERT INTO questionbase VALUES(?,?)");
        
                pst.setString(1,secretquest);
                pst.setString(2,ownsecretquest);
        
                pst.executeUpdate();
           }
           String newdate=DateWrapper.parseDate(new Date());
            
           //st.executeUpdate("insert into LOGIN_DETAILS values('"+loginid+"','"+password+"','"+firstname+"','"+lastname+"','"+logintype+"',"+status+",'"+newdate+"',"+secretquest+",'"+secretans+"',"+firstlogin+",'"+newdate+"')");
           pst=con.prepareStatement("insert into LOGINDETAILS values(?,?,?,?,?,?,?,?,?,?,?)");
                        
           pst.setString(1,loginid);
           pst.setString(2,password);
           pst.setString(3,firstname);
           pst.setString(4,lastname);
           pst.setString(5,logintype);
           pst.setInt(6,status);
           pst.setString(7,newdate);
           pst.setString(8,secretquest);
           pst.setString(9,secretans);
           pst.setInt(10,firstlogin);
           pst.setString(11,newdate);
           i=pst.executeUpdate();
           if(i==1)
           {
                pst=con.prepareStatement("insert into LOGINPROFILE values(?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1,loginid);
                pst.setString(2,bdate);
                pst.setString(3, hno);
                pst.setString(4, street);
                pst.setString(5,city);
                pst.setString(6,state);
                pst.setString(7,country);
                pst.setString(8, pincode);
                pst.setString(9, phoneno);
                pst.setString(10, email);                
                pst.setString(11,locale);
                pst.setString(12,newdate);
                i=pst.executeUpdate();
            }
            if(i==1)
            {
                flag=true;
                con.commit();
            }
            else
            {
                flag=false;
                con.rollback();
            } 
            con.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            flag=false;
            try 
            {
                con.rollback();
            } 
            catch (SQLException sex) 
            {
                sex.printStackTrace();
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            flag=false;
            try 
            {
                con.rollback();
            } 
            catch (SQLException se) 
            {
                se.printStackTrace();
            }
        }
        return flag;
    }
    
    //Getting profile
    public Profile getProfile(String loginname)
    {
       Profile rb=null;
        try
        {
        	con=getConnection();
           Statement st = con.createStatement();
           ResultSet rs = st.executeQuery("select ld.firstname,ld.lastname,lp.birthdate,lp.hno,lp.street,lp.city,lp.state,lp.country,lp.pincode,lp.contactno,lp.email from logindetails ld,loginprofile lp where ld.loginname=lp.loginid and ld.loginname='"+loginname+"'");
           if(rs.next())
           {
        	   rb=new Profile();
        	   rb.setLoginID(loginname);
        	   rb.setFirstName(rs.getString(1));
        	   rb.setLastName(rs.getString(2));
        	   rb.setBirthDate1(rs.getDate(3));
        	   rb.setHno(rs.getString(4));
        	   rb.setStreet(rs.getString(5));
        	   rb.setCity(rs.getString(6));
        	   rb.setState(rs.getString(7));
        	   rb.setCountry(rs.getString(8));
        	   rb.setPincode(rs.getString(9));
        	   rb.setPhoneNo(rs.getString(10));
        	   rb.setEmail(rs.getString(11));
           }
           con.close();
        }
        catch(Exception e)
        {
        	LoggerManager.writeLogSevere(e);
        }
        return rb;
    } 
    
    
    
    
    // Modify Profile
    public boolean modifyProfile(Profile regbean)
    {
        String loginid=regbean.getLoginID();
        String hno = regbean.getHno();
        String street = regbean.getStreet();
        String bdate=DateWrapper.parseDate(regbean.getBirthDate());
        String city=regbean.getCity();
        String state=regbean.getState();
        String country=regbean.getCountry();
        String pincode = regbean.getPincode();
        String contactno = regbean.getPhoneNo();
        String email = regbean.getEmail();
        
        String firstname=regbean.getFirstName();
        String lastname=regbean.getLastName();
        
        
        try 
        {
        	con=getConnection();
            con.setAutoCommit(false);
            PreparedStatement pst=con.prepareStatement("UPDATE loginprofile SET birthdate=?,hno=?,street=?,city=?,state=?,country=?,pincode=?,contactno=?,email=?,profilemodifieddate=? WHERE loginid=?");
            PreparedStatement pst1=con.prepareStatement("UPDATE logindetails SET firstname=?,lastname=? WHERE loginname=?");
            
            pst.setString(1,bdate);
            pst.setString(2, hno);
            pst.setString(3, street);
            pst.setString(4,city);
            pst.setString(5,state);
            pst.setString(6,country);
            pst.setString(7, pincode);
            pst.setString(8, contactno);
            pst.setString(9, email);
            pst.setString(10, DateWrapper.parseDate(new Date()));
            pst.setString(11,loginid);
           
            pst1.setString(1, firstname);
            pst1.setString(2, lastname);
            pst1.setString(3,loginid);
            
            int i=pst.executeUpdate();
            
            if(i!=0)
            {
            	i=pst1.executeUpdate();
            	if(i!=0)
            	{
                flag=true;
                con.commit();
            	}
            	else
            	{
            		flag=false;
            		con.rollback();
            	}
            }
            else
            {
                flag=false;
                con.rollback();
            }
            con.close();
        } 
        catch (SQLException ex) 
        {
        	ex.printStackTrace();
            LoggerManager.writeLogSevere(ex);
            flag=false;
            try 
            {
                con.rollback();
            } 
            catch (SQLException se) 
            {
                LoggerManager.writeLogSevere(se);
            }
        }
        catch (Exception e) 
        {
        	LoggerManager.writeLogSevere(e);
            flag=false;
            try 
            {
                con.rollback();
            } 
            catch (SQLException se) 
            {
            	LoggerManager.writeLogSevere(se);
            }
        }
        return flag;
    }
    
    public boolean changeAccountStatus(String loginid,int status)
    {
        try 
        {
        	con=getConnection();
            con.setAutoCommit(false);
            if(status == 0)
            	status = 1;
            else
            	status = 0;
            PreparedStatement pst=con.prepareStatement("UPDATE logindetails SET loginstatus=? WHERE loginname=?");
            
            pst.setInt(1,status);
            pst.setString(2,loginid);
            int i=pst.executeUpdate();

            if(i==1)
            {
                flag=true;
                con.commit();
            }
            else
            {
                flag=false;
                con.rollback();
            }
            con.close();
        } 
        catch (SQLException ex) 
        {
        	LoggerManager.writeLogSevere(ex);
            flag=false;
            try 
            {
                con.rollback();
            } 
            catch (SQLException se) 
            {
            	LoggerManager.writeLogSevere(se);
            }
        }
        catch (Exception e) 
        {
        	LoggerManager.writeLogSevere(e);
            flag=false;
            try 
            {
                con.rollback();
            } 
            catch (SQLException se) 
            {
            	LoggerManager.writeLogSevere(se);
            }
        }
        return flag;
    }
}
