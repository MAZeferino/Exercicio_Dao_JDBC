package model.dao;

import model.entities.Department;
import model.entities.Seller;
import Exceptions.DBException;

import java.util.List;

public interface SellerDao {
    public void insert(Seller obj);
    public void update(Seller obj);
    public void deleteById(Integer id);
    public Seller findById(Integer id) throws DBException;
    public List<Seller> findByDepartment(Department department);
    public List<Seller> findAll();
}
