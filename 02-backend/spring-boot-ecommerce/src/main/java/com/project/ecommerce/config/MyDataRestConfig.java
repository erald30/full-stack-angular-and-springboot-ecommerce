package com.project.ecommerce.config;

import com.project.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//import org.springframework.web.servlet.config.annotation.CorsRegistry;
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    @Value("${allowed.origins}")
    private String[] theAllowedOrigins;

    private final EntityManager entityManager;

    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnsupportedMethods = {
                HttpMethod.POST,HttpMethod.PUT,HttpMethod.DELETE,HttpMethod.PATCH};

        // disable the HTTP methods for Product : PUT,POST,DELETE
        disabledHttpMethods(config,theUnsupportedMethods, Product.class);
        // disable the HTTP methods for ProductCategory : PUT,POST,DELETE
        disabledHttpMethods(config, theUnsupportedMethods,ProductCategory.class);

        // disable the HTTP methods for State : PUT,POST,DELETE
        disabledHttpMethods(config,theUnsupportedMethods, State.class);

        // disable the HTTP methods for Country : PUT,POST,DELETE
        disabledHttpMethods(config,theUnsupportedMethods, Country.class);

        // disable the HTTP methods for Order : PUT,POST,DELETE
        disabledHttpMethods(config,theUnsupportedMethods, Order.class);


        // call an internal helper method
        exposeId(config);

        // configure cors mapping
        cors.addMapping(config.getBasePath()+"/**").allowedOrigins(theAllowedOrigins);
    }

    private static void disabledHttpMethods(RepositoryRestConfiguration config, HttpMethod[] theUnsupportedMethods, Class theClass) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedMethods));
    }

    private void exposeId(RepositoryRestConfiguration config) {
        // expose entity id-s

        // get a list of all entity classes from EntityManager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create an array of entity types
        List<Class> entityClasses = new ArrayList<>();

        // get the entity types for the entities
        for (EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        // expose the entity id-s for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);

    }
}
