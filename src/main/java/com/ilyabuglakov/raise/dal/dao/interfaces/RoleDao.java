package com.ilyabuglakov.raise.dal.dao.interfaces;

import com.ilyabuglakov.raise.dal.dao.Dao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.Role;
import com.ilyabuglakov.raise.domain.type.UserRole;

import java.util.Set;

/**
 * The interface Role dao.
 */
public interface RoleDao extends Dao<Role> {
    /**
     * Find permissions set.
     *
     * @param id the id
     * @return the set
     * @throws DaoOperationException the dao operation exception
     */
    Set<String> findPermissions(Integer id) throws DaoOperationException;

    /**
     * Find user roles set.
     *
     * @param userId the user id
     * @return the set
     * @throws DaoOperationException the dao operation exception
     */
    Set<UserRole> findUserRoles(Integer userId) throws DaoOperationException;

    /**
     * Create user roles.
     *
     * @param userId    the user id
     * @param userRoles the user roles
     * @throws DaoOperationException the dao operation exception
     */
    void createUserRoles(Integer userId, Set<UserRole> userRoles) throws DaoOperationException;
}
