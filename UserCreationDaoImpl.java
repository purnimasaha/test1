package com.mpa.service.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mpa.service.dao.IUserCreationDao;
import com.mpa.service.model.CodeValue;
import com.mpa.service.model.User;
import com.mpa.service.model.UserTypeMaster;
import com.mpa.service.util.QueryConstant;
import com.mpa.service.util.ServiceConstant;
import com.mpa.service.util.ServiceResponse;

@Repository
public class UserCreationDaoImpl implements IUserCreationDao {
	private static final Logger logger = Logger.getLogger(UserCreationDaoImpl.class);
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public String saveUpdateUser(User user) {
				
		String status = "";
		if(user.getId()<=0){
			status=saveUser(user);	
		}else if(user.getId()>0){
			status=updateUser(user);	
		}else{
			
		}
		
		return status;
	}

	
	public String saveUser(User user) {
		String status = "false";
		 int row =0;
		
		int createdBy=user.getCreatedBy();
		Date createdOn=new Date();
		int updatedBy=user.getCreatedBy();
		Date updatedOn=new Date();
		
		try {
		    
			
			java.util.Date today = new java.util.Date();
			//boolean status = false;
			logger.debug("Inside saveUserCreation dao layer");
			String sql = QueryConstant.SAVE_USER_CREATION_QUERY;
			Object[] params = new Object[] { user.getUserName(),user.getFirstName() , user.getMiddleName() , user.getLastName() , user.getEmail(), user.getAlternateEmail(), user.getCountryId() 
					, user.getContactNumber() , user.getPassword() , user.getSecurityQuestion1() , user.getSecurityQuestion2() , user.getSecurityAnswer1() , user.getSecurityAnswer2() 
					, user.getStatus() , user.getImgPath(), new Timestamp(today.getTime()) , user.getCreatedBy() , new Timestamp(today.getTime()) , user.getUpdatedBy() };

			int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, 
					Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.VARCHAR,Types.TIMESTAMP,Types.VARCHAR};

			row = jdbcTemplate.update(sql, params, types);
			if (row == 1) {
				status = "true";
			}
			
		} catch (Exception ex) {
			status = "false";
			ex.printStackTrace();
		}
		
/*		
		if(row>0){	
			Long id = jdbcTemplate.queryForObject("select max(id) as maxId from users",Long.class);
			
			for(String projectId:user.getProjects()){
				Object param1[] = null;
				int returnValue1 =0;
				try {
					
				    param1 = new Object[] {id,projectId,createdBy, createdOn,updatedBy, updatedOn};
				    String query1=  "insert into user_project(user_id,project_id,created_by,created_on,updated_by,updated_on) values(?,?,?,?,?,?);";
				    returnValue1 = jdbcTemplate.update(query1, param1);
				   
					
				} catch (Exception ex) {
					status = "false";
					ex.printStackTrace();
				}
			}
			
			for(String roleId:user.getRoles()){
				Object param2[] = null;
				int returnValue2 =0;
				
				try {
					
				    param2 = new Object[] {id,roleId, createdOn,createdBy,updatedBy, updatedOn};
				    String query2=  "insert into user_roles(user_id,role_master_id,created_date,created_by,updated_by,updated_date) values(?,?,?,?,?,?);";
				    returnValue2 = jdbcTemplate.update(query2, param2);
				   
					
				} catch (Exception ex) {
					status = "false";
					ex.printStackTrace();
				}
			}
		}*/
		
		return status;
	}
	
	public String updateUser(User user) {
		String status = "true";
		 int returnValue =0;
		
		//String userRole=user.getRole();
		//String userProject=user.getProject();
		
		int createdBy=user.getCreatedBy();
		Date createdOn=new Date();
		int updatedBy=user.getCreatedBy();
		Date updatedOn=new Date();
		Object param[] = null;
		
		try {
			
		    java.util.Date today = new java.util.Date();
		    
			param = new Object[] {user.getUserName(),user.getFirstName(),user.getMiddleName(),user.getLastName(),user.getEmail(), user.getAlternateEmail(), user.getCountryId(), 
					user.getContactNumber(),user.getSecurityQuestion1(),user.getSecurityQuestion2(),user.getSecurityAnswer1(),user.getSecurityAnswer2(),
					user.getStatus(),user.getImgPath(),new Timestamp(today.getTime()),user.getUpdatedBy(),user.getId()};

			returnValue = jdbcTemplate.update(QueryConstant.UPDATE_USER_CREATION_QUERY, param);
		   
			
		} catch (Exception ex) {
			status = "false";
			ex.printStackTrace();
		}
		
		
/*		if(returnValue>0){	
			*//*************** For Role Update *************//*
			List<String> newRoles=new ArrayList<String>();
			newRoles.addAll(user.getRoles());
			
    		List<String> exestingRoles=new ArrayList<String>();
    		
    		String query2 ="select rm.role_master_id from role_master rm,user_roles ur where ur.role_master_id=rm.role_master_id and ur.user_id="+user.getId();
    		List<Map<String, Object>> rows2 = jdbcTemplate.queryForList(query2);
    		
        	if(rows2!=null && rows2.size()>0){
        		for(int i1=0; i1<rows2.size();i1++){
	        		Map row2 = rows2.get(i1);
	        		exestingRoles.add((int)row2.get("role_master_id")+"");
        		}
        	}
        	
        	Set<String> newAndExestingRoles=new HashSet<String>();
        	newAndExestingRoles.addAll(exestingRoles);
        	newAndExestingRoles.addAll(newRoles);
        	
        	List<String> deleteRoles=new ArrayList<String>(newAndExestingRoles);
        	deleteRoles.removeAll(newRoles);
        	
        	List<String> addRoles=new ArrayList<String>(newAndExestingRoles);
        	addRoles.removeAll(exestingRoles);
        	
        	if(deleteRoles!=null && deleteRoles.size()>0){
        		for(String delId:deleteRoles){
            		String deleteRoleQuery=  "delete from user_roles where role_master_id="+delId+" and user_id="+user.getId();
            		int deletedFlag = jdbcTemplate.update(deleteRoleQuery);	
        		}

        	}
        	
        	if(addRoles!=null && addRoles.size()>0){
        		for(String addId:addRoles){
    				Object addRoleParam[] = null;  				
    				try {  					
    					addRoleParam = new Object[] {user.getId(),addId, createdOn,createdBy,updatedBy, updatedOn};
    				    String addRoleQuery=  "insert into user_roles(user_id,role_master_id,created_date,created_by,updated_by,updated_date) values(?,?,?,?,?,?);";
    				    int addRoleFlag = jdbcTemplate.update(addRoleQuery, addRoleParam);  				   
    					
    				} catch (Exception ex) {
    					status = "false";
    					ex.printStackTrace();
    				}
        		}
        	}			
			
			*//*************** For Project Update *************//*
			List<String> newProjects=new ArrayList<String>();
			newProjects.addAll(user.getProjects());
			
    		List<String> exestingProjects=new ArrayList<String>();
    		
    		String queryProject ="select p.project_id from project p,user_project up where up.project_id=p.project_id and up.user_id="+user.getId();
    		List<Map<String, Object>> rows3 = jdbcTemplate.queryForList(queryProject);
    		
        	if(rows3!=null && rows3.size()>0){
        		for(int i1=0; i1<rows3.size();i1++){
	        		Map row3 = rows3.get(i1);
	        		exestingProjects.add((int)row3.get("project_id")+"");
        		}
        	}
        	
        	Set<String> newAndExestingProjects=new HashSet<String>();
        	newAndExestingProjects.addAll(exestingProjects);
        	newAndExestingProjects.addAll(newProjects);
        	
        	List<String> deleteProjects=new ArrayList<String>(newAndExestingProjects);
        	deleteProjects.removeAll(newProjects);
        	
        	List<String> addProjects=new ArrayList<String>(newAndExestingProjects);
        	addProjects.removeAll(exestingProjects);
        	
        	if(deleteProjects!=null && deleteProjects.size()>0){
        		for(String delProjId:deleteProjects){
            		String deleteProjectQuery=  "delete from user_project where project_id="+delProjId+" and user_id="+user.getId();
            		int deletedProjectFlag = jdbcTemplate.update(deleteProjectQuery);	
        		}

        	}
        	
        	if(addProjects!=null && addProjects.size()>0){
        		for(String addProjId:addProjects){
    				Object addProjectParam[] = null;  				
    				try {  					
    					addProjectParam = new Object[] {user.getId(),addProjId, createdBy,createdOn,updatedBy, updatedOn};
    					String addProjectQuery=  "insert into user_project(user_id,project_id,created_by,created_on,updated_by,updated_on) values(?,?,?,?,?,?);";
    				    int addProjectFlag = jdbcTemplate.update(addProjectQuery, addProjectParam);  				   
    					
    				} catch (Exception ex) {
    					status = "false";
    					ex.printStackTrace();
    				}
        		}
        	}
        	
        	
			
		}*/
		
		return status;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> listUser(){
		List<User> users=new ArrayList<User>();
		
		
		
		 String query = "select u.* , cm.countries_name from users u,countries_master cm where u.countries_id=cm.countries_id";
		 
	        try {
	        	List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

	        	if(rows!=null && rows.size()>0){
	        		for(int i=0; i<rows.size();i++){
		        		Map row = rows.get(i);
		        		User user=new User();
		        		
		        		user.setId((int)row.get("id"));
		        		user.setUserName((String)row.get("user_name"));
		        		user.setFirstName((String)row.get("f_name"));
		        		user.setMiddleName((String)row.get("m_name"));
		        		user.setLastName((String)row.get("l_name"));
		        		user.setEmail((String)row.get("email_id"));
		        		user.setAlternateEmail((String)row.get("alternate_email_id"));
		        		user.setContactNumber((String)row.get("contact_number"));
		        		String status=(String)row.get("status");
		        		
		        		//user.setRoleName((String)row.get("authority"));
		        		
		        		
		        		if(status.equals("1")){
		        			user.setStatus("Active");	
		        		}else{
		        			user.setStatus("Inactive");	
		        		}
		        		
/*		        		
		        		String roles=null;
		        		String query2 ="select rm.authority from role_master rm,user_roles ur where ur.role_master_id=rm.role_master_id and ur.user_id="+user.getId();
		        		List<Map<String, Object>> rows2 = jdbcTemplate.queryForList(query2);
		        		
			        	if(rows2!=null && rows2.size()>0){
			        		for(int i1=0; i1<rows2.size();i1++){
				        		Map row2 = rows2.get(i1);
				        		if(roles==null){
				        			roles=(String)row2.get("authority");
				        		}else{
				        			roles=roles+" , "+(String)row2.get("authority");
				        		}
			        		}
			        	}
			        	
			        	user.setRoleNames(roles);
		        		
		        		
		        		String projects=null;
		        		String query1 ="select p.proj_name from project p,user_project up where up.project_id=p.project_id and up.user_id="+user.getId();
		        		List<Map<String, Object>> rows1 = jdbcTemplate.queryForList(query1);
		        		
			        	if(rows1!=null && rows1.size()>0){
			        		for(int i1=0; i1<rows1.size();i1++){
				        		Map row1 = rows1.get(i1);
				        		if(projects==null){
				        			projects=(String)row1.get("proj_name");
				        		}else{
				        			projects=projects+" , "+(String)row1.get("proj_name");
				        		}
			        		}
			        	}
*/			        	
			        	//user.setProjectNames(projects);
			        	user.setCountryName((String)row.get("countries_name"));
			        	
		        		users.add(user);
	        		}
	        	}


	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		
		
		
		return users;
	}
	

	
	public String deleteUser(int userId){
		String status = "";
		int returnValue1=0;
		
		String query="update users set status=0 where id="+userId;
		try {
		    returnValue1 = jdbcTemplate.update(query);
		} catch (Exception ex) {
			status = "false";
			ex.printStackTrace();
		}
		if(returnValue1>0){
			status = "true";	
		}
		
		return status;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public User getUserByID(int userId){
		User user=new User();

/*		String query ="select  u.id,u.user_name,u.f_name,u.m_name,u.l_name,u.email_id,u.contact_number, u.sec_q1,u.sec_q2, u.ans1, u.ans2,   u.status,u.img_path, rm.role_master_id, rm.authority "
				+ "from users u, user_roles ur, role_master rm where u.id=ur.user_id and ur.role_master_id=rm.role_master_id and u.id="+userId;
*/
		String query ="select  u.id,u.user_name,u.f_name,u.m_name,u.l_name,u.email_id,u.alternate_email_id,u.contact_number, u.sec_q1,u.sec_q2, u.ans1, u.ans2,   u.status,u.img_path, cm.countries_id,cm.countries_name"
				+ " from users u, countries_master cm where u.countries_id=cm.countries_id and u.id="+userId;

		try {
	        	List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

	        	if(rows!=null && rows.size()>0){
	        		
		        		Map row = rows.get(0);
		        		
		        		user.setId((int)row.get("id"));
		        		user.setUserName((String)row.get("user_name"));
		        		//user.setPassword((String)row.get("password"));
		        		user.setFirstName((String)row.get("f_name"));
		        		user.setMiddleName((String)row.get("m_name"));
		        		user.setLastName((String)row.get("l_name"));
		        		user.setEmail((String)row.get("email_id"));
		        		user.setAlternateEmail((String)row.get("alternate_email_id"));
		        		user.setContactNumber((String)row.get("contact_number"));
		        		user.setStatus((String)row.get("status"));
		        		
		        		//user.setRoleName((String)row.get("authority"));
		        		//user.setRole((int)row.get("role_master_id")+"");
		        		
		        		user.setSecurityQuestion1((int)row.get("sec_q1")+"");
		        		user.setSecurityQuestion2((int)row.get("sec_q2")+"");
		        		
		        		user.setSecurityAnswer1((String)row.get("ans1"));
		        		user.setSecurityAnswer2((String)row.get("ans2"));
		        		
		        		//user.setImgPath((String)row.get("img_path"));
		        		
		        		
/*		        		
		        		List<String> roles=new ArrayList<String>();
		        		
		        		String query2 ="select rm.role_master_id from role_master rm,user_roles ur where ur.role_master_id=rm.role_master_id and ur.user_id="+user.getId();
		        		List<Map<String, Object>> rows2 = jdbcTemplate.queryForList(query2);
		        		
			        	if(rows2!=null && rows2.size()>0){
			        		for(int i1=0; i1<rows2.size();i1++){
				        		Map row2 = rows2.get(i1);
				        		roles.add((int)row2.get("role_master_id")+"");
			        		}
			        	}
			        	user.setRoles(roles);
		        		
		        		
		        		
		        		List<String> projects=new ArrayList<String>();
		        		
		        		String query1 ="select p.project_id from project p,user_project up where up.project_id=p.project_id and up.user_id="+user.getId();
		        		List<Map<String, Object>> rows1 = jdbcTemplate.queryForList(query1);
		        		
			        	if(rows1!=null && rows1.size()>0){
			        		for(int i1=0; i1<rows1.size();i1++){
				        		Map row1 = rows1.get(i1);
				        		projects.add((int)row1.get("project_id")+"");
			        		}
			        	}
			        	user.setProjects(projects);*/
			        	
			        	user.setCountryId((int)row.get("countries_id")+"");

	        		
	        	}


	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		
		
		
		return user;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ServiceResponse getUserDetails() {
		ServiceResponse serviceResponse = new ServiceResponse();
		List<CodeValue> dropDownList = new ArrayList<CodeValue>();
		try {
			System.out.println("getUserDetails");
			dropDownList = jdbcTemplate.query("SELECT distinct(id),CONCAT(f_name,' ',l_name) as userName FROM users ", new RowMapper() {
				public Object mapRow(ResultSet rs, int row) throws SQLException {
					CodeValue codeValue = new CodeValue();
					codeValue.setCode(rs.getInt("id"));
					codeValue.setValue(rs.getString("userName"));
					return codeValue;
				}
			});
			serviceResponse.setResObject(dropDownList);
			serviceResponse.setStatus(ServiceConstant.RESPONSE_SUCCESS);
			serviceResponse.setResponseMessage(ServiceConstant.RESPONSE_SUCCESS);
			serviceResponse.setSearchCount(dropDownList.size());
		} catch (Exception e) {
			e.printStackTrace();
			serviceResponse.setResObject(null);
			serviceResponse.setStatus(ServiceConstant.RESPONSE_FAIL);
			serviceResponse.setResponseMessage(ServiceConstant.RESPONSE_FAIL);
		}
		return serviceResponse;
	}

	
}
