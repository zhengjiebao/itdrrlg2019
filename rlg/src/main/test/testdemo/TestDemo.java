package testdemo;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class TestDemo {

    @Test
    public void Test1(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
        DriverManagerDataSource dm = (DriverManagerDataSource) ac.getBean("dataSource");
        String url = dm.getUrl();
        System.out.println(url);
    }

    @Test
    public void Test2(){
/*        try{
            int i = 100 / 0;
            System.out.print(i);
        }catch(Exception e){
            System.out.print(1);
            throw new RuntimeException();
        }finally{
            System.out.print(2);
        }
            System.out.print(3);*/
    }
}
