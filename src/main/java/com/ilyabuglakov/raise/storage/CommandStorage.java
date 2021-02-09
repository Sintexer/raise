package com.ilyabuglakov.raise.storage;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.command.impl.admin.NewTestsCatalogGetCommand;
import com.ilyabuglakov.raise.command.impl.index.IndexGetCommand;
import com.ilyabuglakov.raise.command.impl.localization.ChangeLocalizationCommand;
import com.ilyabuglakov.raise.command.impl.login.LoginGetCommand;
import com.ilyabuglakov.raise.command.impl.login.LoginPostCommand;
import com.ilyabuglakov.raise.command.impl.registration.ConfirmAccountCommand;
import com.ilyabuglakov.raise.command.impl.registration.ConfirmCheckEmailGetCommand;
import com.ilyabuglakov.raise.command.impl.registration.RegistrationGetCommand;
import com.ilyabuglakov.raise.command.impl.registration.RegistrationPostCommand;
import com.ilyabuglakov.raise.command.impl.rest.BanTestCommand;
import com.ilyabuglakov.raise.command.impl.rest.ConfirmTestCommand;
import com.ilyabuglakov.raise.command.impl.rest.DeleteCommentCommand;
import com.ilyabuglakov.raise.command.impl.test.CommentPostCommand;
import com.ilyabuglakov.raise.command.impl.test.TestCatalogPageCommand;
import com.ilyabuglakov.raise.command.impl.test.TestCreateSuccessGetCommand;
import com.ilyabuglakov.raise.command.impl.test.TestCreatorGetCommand;
import com.ilyabuglakov.raise.command.impl.test.TestCreatorSaveCommand;
import com.ilyabuglakov.raise.command.impl.test.TestPreviewPageCommand;
import com.ilyabuglakov.raise.command.impl.test.TestResultCommand;
import com.ilyabuglakov.raise.command.impl.test.TestingGetCommand;
import com.ilyabuglakov.raise.command.impl.user.UserProfileChangeGetCommand;
import com.ilyabuglakov.raise.command.impl.user.UserProfileChangeSaveCommand;
import com.ilyabuglakov.raise.command.impl.user.UserProfileGetCommand;
import com.ilyabuglakov.raise.model.RequestMethod;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Command storage. Stores command instances by request methods.
 */
public class CommandStorage {

    private final EnumMap<RequestMethod, Map<String, Command>> methodMap;

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static final CommandStorage INSTANCE = new CommandStorage();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static CommandStorage getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private CommandStorage() {
        methodMap = new EnumMap<>(RequestMethod.class);
        for (RequestMethod value : RequestMethod.values()) {
            methodMap.put(value, new HashMap<>());
        }

        addCommand(RequestMethod.GET, "root", new IndexGetCommand());
        addCommand(RequestMethod.GET, "index", new IndexGetCommand());
        addCommand(RequestMethod.GET, "registration", new RegistrationGetCommand());
        addCommand(RequestMethod.GET, "changeLocale", new ChangeLocalizationCommand());
        addCommand(RequestMethod.GET, "test.creator", new TestCreatorGetCommand());
        addCommand(RequestMethod.GET, "test.creator.save.success", new TestCreateSuccessGetCommand());
        addCommand(RequestMethod.GET, "test.catalog", new TestCatalogPageCommand());
        addCommand(RequestMethod.GET, "test.testing", new TestingGetCommand());
        addCommand(RequestMethod.GET, "test.preview", new TestPreviewPageCommand());
        addCommand(RequestMethod.GET, "login", new LoginGetCommand());
        addCommand(RequestMethod.GET, "profile", new UserProfileGetCommand());
        addCommand(RequestMethod.GET, "user.profile.change", new UserProfileChangeGetCommand());
        addCommand(RequestMethod.GET, "admin.test.catalog", new NewTestsCatalogGetCommand());
        addCommand(RequestMethod.GET, "auth.confirm", new ConfirmAccountCommand());
        addCommand(RequestMethod.GET, "auth.confirm.email", new ConfirmCheckEmailGetCommand());

        addCommand(RequestMethod.POST, "registration", new RegistrationPostCommand());
        addCommand(RequestMethod.POST, "test.creator.save", new TestCreatorSaveCommand());
        addCommand(RequestMethod.POST, "test.testing.result", new TestResultCommand());
        addCommand(RequestMethod.POST, "login", new LoginPostCommand());
        addCommand(RequestMethod.POST, "user.profile.change.save", new UserProfileChangeSaveCommand());
        addCommand(RequestMethod.POST, "rest.confirm.test", new ConfirmTestCommand());
        addCommand(RequestMethod.POST, "rest.ban.test", new BanTestCommand());
        addCommand(RequestMethod.POST, "test.preview", new CommentPostCommand());
        addCommand(RequestMethod.POST, "rest.delete.comment", new DeleteCommentCommand());

    }

    private void addCommand(RequestMethod method, String property, Command command) {
        String uri = PropertiesStorage.getInstance().getLinks().getProperty(property);
        methodMap.get(method).put(uri, command);
    }

    /**
     * Gets command.
     *
     * @param link   the link
     * @param method the method
     * @return the command
     */
    public Command getCommand(String link, RequestMethod method) {
        return methodMap.get(method).get(link);
    }


}
