package model.dao.impl;

import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import db.DB;
import Exceptions.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;
    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(
                    "INSERT INTO seller " +
                        "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                        "VALUES " +
                        "(?, ?, ?, ?, ?)"
                    , Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());
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
            System.out.println("Seller added to Database!");
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }finally {
            DB.autoClose(ps);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(
                    "UPDATE seller " +
                        "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                        "WHERE Id = ?"
            );
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());
            ps.setInt(6, obj.getId());
            ps.executeUpdate();
            System.out.println("Seller Updated!");
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }finally {
            DB.autoClose(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(
                    "DELETE FROM seller WHERE Id = ?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Seller removed to Database!");
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }finally {
            DB.autoClose(ps);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName FROM seller "+
                        "INNER JOIN department ON seller.DepartmentId = department.Id "+
                        "WHERE seller.Id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                return instantiateSeller(rs, dep);
            }else{
                System.err.println("Couldnt find a Seller with thi ID");
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
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name DepName FROM seller "+
                        "INNER JOIN department ON seller.DepartmentId = department.Id "+
                        "WHERE DepartmentId = ? "+
                        "ORDER BY Name"
            );
            ps.setInt(1, department.getId());
            rs = ps.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));
                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller sell = instantiateSeller(rs, dep);
                list.add(sell);
            }
            return list;
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally{
            DB.autoClose(ps);
            DB.autoClose(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName FROM seller "+
                            "INNER JOIN department ON seller.DepartmentId = department.Id "+
                            "ORDER BY Name"
            );
            rs = ps.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));
                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller sell = instantiateSeller(rs, dep);
                list.add(sell);
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
        return new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        return new Seller(
                rs.getInt("Id"),
                rs.getString("Name"),
                rs.getString("Email"),
                rs.getDate("BirthDate"),
                rs.getDouble("BaseSalary"),
                dep
        );
    }
}
