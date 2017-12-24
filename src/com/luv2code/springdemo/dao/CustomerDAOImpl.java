package com.luv2code.springdemo.dao;

import com.luv2code.springdemo.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    // need to inject the session factory
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Customer> getCustomers() {

        // get the current hibernate session
        Session session = sessionFactory.getCurrentSession();

        // create a query
        Query<Customer> theQuery = session.createQuery("from Customer ORDER BY lastName", Customer.class);


        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();

        // return the results
        return customers;
    }

    @Override
    public void saveCustomer(Customer customer) {

        // get current hibernate session
        Session session = sessionFactory.getCurrentSession();

        // save/update the customer ... finally LOL
        session.saveOrUpdate(customer);
    }

    @Override
    public Customer getCustomer(int theId) {

        // get the current hibernate session
        Session session = sessionFactory.getCurrentSession();

        // now retrieve/read from database using the primary key
        Customer customer = session.get(Customer.class, theId);

        return customer;
    }

    @Override
    public void deleteCustomer(int theId) {

        // get the current hibernate session
        Session session = sessionFactory.getCurrentSession();

        // delete object with primary key
        Query query = session.createQuery("delete from Customer where id=:customerId");
        query.setParameter("customerId", theId);

        query.executeUpdate();
    }

    @Override
    public List<Customer> searchCustomers(String theSearchName) {
        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        Query theQuery = null;

        //
        // only search by name if theSearchName is not empty
        //
        if (theSearchName != null && theSearchName.trim().length() > 0) {

            // search for firstName or lastName ... case insensitive
            theQuery =currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");

        }
        else {
            // theSearchName is empty ... so just get all customers
            theQuery =currentSession.createQuery("from Customer", Customer.class);
        }

        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();

        // return the results
        return customers;
    }
}
