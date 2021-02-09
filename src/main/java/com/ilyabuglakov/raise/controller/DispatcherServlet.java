package com.ilyabuglakov.raise.controller;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.command.manager.CommandManager;
import com.ilyabuglakov.raise.command.manager.CommandManagerFactory;
import com.ilyabuglakov.raise.config.ApplicationConfig;
import com.ilyabuglakov.raise.config.exception.PoolConfigurationException;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.dal.transaction.factory.impl.DatabaseTransactionFactory;
import com.ilyabuglakov.raise.model.LocaleType;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.factory.ServiceFactory;
import com.ilyabuglakov.raise.model.service.domain.factory.database.DatabaseServiceFactory;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * The type Dispatcher servlet.
 * <p>
 * Controls application requests and runs commands
 */
@Log4j2
@WebServlet("/controller")
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        try {
            ApplicationConfig.initConnectionPool();
        } catch (PoolConfigurationException e) {
            log.fatal("Can't init ConnectionPool through application.properties: " + e.getMessage(), e);
            destroy();
        }
    }

    @Override
    public void destroy() {
        ApplicationConfig.closeConnectionPool();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Entered get");
        processCommand(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Entered post");
        processCommand(req, resp);
    }

    private ServiceFactory getServiceFactory() {
        return new DatabaseServiceFactory(new DatabaseTransactionFactory());
    }

    private Optional<Command> extractCommand(HttpServletRequest request) {
        return Optional.ofNullable((Command) request.getAttribute("command"));
    }

    private void processCommand(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Command> command = extractCommand(req);
        CommandManager commandManager = CommandManagerFactory.getCommandManager(getServiceFactory());
        try {
            if (command.isPresent()) {
                req.setAttribute("locales", Arrays.asList(LocaleType.values()));
                ResponseEntity responseEntity = commandManager.execute(command.get(), req, resp);
                commandManager.close();
                if (responseEntity != null)
                    processResponseEntity(responseEntity, req, resp);
            } else {
                commandManager.rollback();
                resp.sendError(404);
            }

        } catch (PersistentException e) {
            log.error(e);
            commandManager.rollback();
            resp.sendError(500);
        }
    }

    private void processResponseEntity(ResponseEntity responseEntity,
                                       HttpServletRequest request,
                                       HttpServletResponse response)
            throws IOException, ServletException {
        if (responseEntity.isRedirect()) {
            log.debug("Send redirect " + responseEntity.getLink());
            response.sendRedirect(responseEntity.getLink());
        } else {
            responseEntity.getAttributes().forEach(request::setAttribute);
            request.getRequestDispatcher(responseEntity.getLink()).forward(request, response);
        }
    }

}
