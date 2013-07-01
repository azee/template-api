package com.mycompany.template.repositories.custom.impl;

import com.mycompany.template.beans.SomeBean;
import com.mycompany.template.repositories.custom.SomeBeanRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 02.11.12
 * Time: 12:35
 *
 * Implementation of custom operation over SomeBean repository
 */
public class SomeBeanRepositoryCustomImpl implements SomeBeanRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public List<SomeBean> findLimited(int skip, int limit) {
        Query query = new Query();
        query.sort().on("time", Order.DESCENDING);
        query.limit(limit);
        query.skip(skip);
        return mongoOperations.find(query, SomeBean.class);
    }

    @Override
    public List<SomeBean> findLimitedSimple(int skip, int limit) {
        Query query = new Query();
        query.sort().on("time", Order.DESCENDING);
        query.limit(limit);
        query.skip(skip);
        query.fields().exclude("properties");
        return mongoOperations.find(query, SomeBean.class);
    }


    @Override
    public List<SomeBean> findFiltered(int skip, int limit, String title, long createdAfter) {
        Query query = new Query();
        query.sort().on("time", Order.DESCENDING);
        query.limit(limit);
        query.skip(skip);

        try{
            //Add criterias
            if (title != null && !"".equals(title)){
                query.addCriteria(Criteria.where("title").is(title));
            }

            if (createdAfter != 0){
                query.addCriteria(Criteria.where("time").gte(createdAfter));
            }

            //Get filtered values from DB
            return mongoOperations.find(query,SomeBean.class);

        }catch (Exception e){
            return new ArrayList<SomeBean>();
        }
    }

}
