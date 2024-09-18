package configuration;

//import org.apache.ibatis.session.Configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.hikari")
    public HikariConfig hikariconfig(){
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() throws Exception{
        DataSource dataSource = new HikariDataSource(hikariconfig());
        System.out.println(dataSource.toString());
        return dataSource;
    }

    // ====== 기존 MyBatis 관련 설정 제거 =======
    // MyBatis 설정 제거 - JPA 사용 시 불필요
    //    @Bean
    //    @ConfigurationProperties(prefix="mybatis.configuration")
    //    public org.apache.ibatis.session.Configuration mybatisConfig(){
    //        return new org.apache.ibatis.session.Configuration();
    //    }


    // MyBatis SqlSessionFactory 제거
    //    @Bean
    //    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
    //        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    //        sqlSessionFactoryBean.setDataSource(dataSource);
    //        sqlSessionFactoryBean.setMapperLocations(
    //                applicationContext.getResources("classpath:/mapper/**/sql-*.xml")
    //        );
    //        sqlSessionFactoryBean.setConfiguration(mybatisConfig());
    //        return sqlSessionFactoryBean.getObject();
    //    }


    // MyBatis SqlSessionTemplate 제거
    //    @Bean
    //    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    //        return new SqlSessionTemplate(sqlSessionFactory);
    //    }


    // ====== JPA 설정 추가 =======
    // JPA Hibernate 설정 추가
    @Bean
    @ConfigurationProperties(prefix="spring.jpa")
    public Properties hibernateConfig(){
        return new Properties();
    }

    // JPA EntityManagerFactory 설정 추가
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[] { "com.example.entity" });  // 엔티티 클래스들이 위치한 패키지 경로

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateConfig());

        return em;
    }

    // JPA TransactionManager 설정 추가
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
