package com.mycompany.template.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.mycompany.template.beans.Property;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 02.11.12
 * Time: 10:46
 *
 * Basic repository for Propertie
 */
public interface PropertyRepository extends PagingAndSortingRepository<Property, String> {

}
