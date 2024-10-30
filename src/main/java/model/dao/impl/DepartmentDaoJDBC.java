package model.dao.impl;

import Exceptions.DBException;
import db.DB;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;
    private Scanner scan;
    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Department obj) {
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(
                    "INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, obj.getName());
            int rows = ps.executeUpdate();

            if(rows > 0){
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.autoClose(rs);
            }else{
                throw new DBException("Unexpected error, no rows updated");
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }finally {
            DB.autoClose(ps);
        }
    }

    @Override
    public void update(Department obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement("SELECT department.* FROM department WHERE Id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                return instantiateDepartment(rs);
            }else{
                System.err.println("Couldnt find a department with thi ID");
            }
            return null;
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally{
            DB.autoClose(ps);
            DB.autoClose(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(
                    "SELECT department.* FROM department ORDER BY Name"
            );
            rs = ps.executeQuery();

            List<Department> list = new ArrayList<>();
            while(rs.next()){
                Department dep = instantiateDepartment(rs);
                list.add(dep);
            }
            return list;
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally{
            DB.autoClose(ps);
            DB.autoClose(rs);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        return new Department(rs.getInt("Id"), rs.getString("Name"));
    }
}
