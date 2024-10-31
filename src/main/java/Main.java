import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ParseException {
        Scanner scan = new Scanner(System.in);

        int choice = 0;
        int entity = 0;

        do {
            System.out.println("\n+=====================+");
            System.out.println("|  Choose the Entity  |");
            System.out.println("+=====================+");
            System.out.println("[1] - Seller");
            System.out.println("[2] - Department");
            System.out.println("[0] - Sair");
            entity = scan.nextInt();
            if(entity != 0) {
                System.out.println("\n+===================+");
                System.out.println("|  Choose the Action  |");
                System.out.println("+=====================+");
                System.out.println("[1] - Insert new");
                System.out.println("[2] - Update");
                System.out.println("[3] - Delete");
                System.out.println("[4] - List All");
                System.out.println("[5] - Find by id");
                if (entity == 1) System.out.println("[6] - Find by department");
                choice = scan.nextInt();
                menuService(choice, entity, scan);
            }
        }while(entity != 0);
    }

    private static void menuService(int choice, int entity, Scanner scan) throws ParseException {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        switch(choice) {
            case 1:
                if ((entity == 1)) {
                    sellerDao.insert(intanciateSeller(scan, sellerDao, departmentDao, false));
                }else{
                    departmentDao.insert(intanciateDepartment(scan, departmentDao, false));
                }
                break;
            case 2:
                if((entity == 1)){
                    sellerDao.update(intanciateSeller(scan, sellerDao, departmentDao, true));
                }else{
                    departmentDao.update(intanciateDepartment(scan, departmentDao, true));
                }
                break;
            case 3:
                System.out.println("Id of the register that U wanna delete");
                ((entity == 1) ? sellerDao.findAll() : departmentDao.findAll()).forEach(System.out::println);
                int RemoveId = scan.nextInt();
                if ((entity == 1)) {
                    sellerDao.deleteById(RemoveId);
                } else {
                    departmentDao.deleteById(RemoveId);
                }
                break;
            case 4:
                ((entity == 1) ? sellerDao.findAll() : departmentDao.findAll()).forEach(System.out::println);
                break;
            case 5:
                System.out.print("ID that U wanna search:");
                int FindId = scan.nextInt();
                Object result = (entity == 1) ? sellerDao.findById(FindId) : departmentDao.findById(FindId) ;
                System.out.println(result);
                break;
            case 6:
                if (entity == 1) {
                    departmentDao.findAll().forEach(System.out::println);
                    System.out.print("Department ID: ");
                    Department dep = departmentDao.findById(scan.nextInt());
                    sellerDao.findByDepartment(dep).forEach(System.out::println);
                }
                break;
            default:
                System.out.println("Opção Inválida");
        }

    }

    public static Seller intanciateSeller(Scanner scan, SellerDao sellerDao, DepartmentDao departmentDao, boolean update) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Seller seller = null;

        if(update){
            System.out.println("ID of the seller that U wanna update");
            seller = sellerDao.findById(scan.nextInt());
            scan.nextLine();
        }

        System.out.print("Name: ");
        String name = scan.nextLine();
        System.out.print("Email: ");
        String email = scan.nextLine();
        System.out.print("Birth Date: ");
        String strDate = scan.nextLine();
        Date bDate = (!strDate.isEmpty()) ? sdf.parse(strDate) : null;
        System.out.print("Base Salary: ");
        Double bSalaray = scan.nextDouble();
        System.out.println("For which department do you wanna add your seller?");
        departmentDao.findAll().forEach(System.out::println);
        System.out.print("Department ID: ");
        Department dep = departmentDao.findById(scan.nextInt());

        if(update){
            seller.setName((name != null) ? name : seller.getName());
            seller.setEmail((email != null) ? email : seller.getEmail());
            seller.setBirthDate((bDate != null) ? bDate : seller.getBirthDate());
            seller.setBaseSalary((bSalaray != null) ? bSalaray : seller.getBaseSalary());
            seller.setDepartment((dep != null) ? dep : seller.getDepartment());
            return seller;
        }else{
            return new Seller(name, email, bDate, bSalaray,dep);
        }

    }

    public static Department intanciateDepartment(Scanner scan, DepartmentDao departmentDao, boolean update) {
        Department department = null;

        if(update){
            System.out.println("ID of the seller that U wanna update");
            department = departmentDao.findById(scan.nextInt());
            scan.nextLine();
        }
        System.out.print("Name: ");
        String name = scan.next();

        if(update){
            department.setName((name != null) ? name : department.getName());
            return department;
        }else{
            return new Department(name);
        }
    }


}
