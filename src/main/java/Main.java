import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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

    private static void menuService(int choice, int entity, Scanner scan){
        SellerDao sellerDao = DaoFactory.createSellerDao();
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        switch(choice) {
            case 1:

//                (entity == 1) ? sellerDao.insert() : DepartmentDao.insert();
                break;
            case 2:
                System.out.print("ID that U wanna search:");
                entity = scan.nextInt();
//                (entity == 1) ? sellerDao.update() : DepartmentDao.update();
                break;
            case 3:
//                (entity == 1) ? sellerDao.deleteById() : DepartmentDao.deleteById();
                break;
            case 4:
                ((entity == 1) ? sellerDao.findAll() : departmentDao.findAll()).forEach(System.out::println);
                break;
            case 5:
                System.out.print("ID that U wanna search:");
                int id = scan.nextInt();
                Object result = (entity == 1) ? sellerDao.findById(id) : departmentDao.findById(id) ;
                System.out.println(result);
                break;
            case 6:
//                (entity == 1) ? sellerDao.findByDepartment();
                break;
            default:
                System.out.println("Opção Inválida");
        }

    }
}
