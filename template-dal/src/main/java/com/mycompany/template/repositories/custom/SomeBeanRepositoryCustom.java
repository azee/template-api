package com.mycompany.template.repositories.custom;


import com.mycompany.template.beans.SomeBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 16.11.12
 * Time: 11:24
 *
 * Custom methods for SomeBean
 */
public interface SomeBeanRepositoryCustom {
    public List<SomeBean> findLimited(int skip, int limit);
    public List<SomeBean> findLimitedSimple(int skip, int limit);
    public List<SomeBean> findFiltered(int skip, int limit, String title, long createdAfter);

}
