package com.ilyabuglakov.raise.command.impl.user;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.dto.UserInfoDto;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.UserService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type User profile change save command.
 * <p>
 * Saves user info changes to storage
 */
@Log4j2
public class UserProfileChangeSaveCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity
     * @throws IOException         by request/response
     * @throws PersistentException datasource error
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistentException {
        Subject subject = SecurityUtils.getSubject();

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .name(request.getParameter("name"))
                .surname(request.getParameter("surname"))
                .oldPassword(request.getParameter("oldPassword"))
                .newPassword(request.getParameter("newPassword"))
                .newPasswordRepeat(request.getParameter("newPasswordRepeat"))
                .build();

        UserService userService = (UserService) serviceFactory.createService(ServiceType.USER);
        User user = userService.getUser((String) subject.getPrincipal()).orElseThrow(PersistentException::new);

        userInfoDto.setUser(user);
        ResponseEntity responseEntity = userService.changeUserInfo(userInfoDto);

        if (responseEntity.isErrorOccurred()) {
            responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("user.profile.change"));
        } else {
            responseEntity.setLink(PropertiesStorage.getInstance().getLinks().getProperty("user.profile.change"));
            responseEntity.setRedirect(true);
        }
        return responseEntity;
    }
}
