package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.OrganizationRepository;
import com.ozay.repository.OrganizationUserRepository;
import com.ozay.repository.UserRepository;
import com.ozay.service.MailService;
import com.ozay.service.OrganizationService;
import com.ozay.service.UserService;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.OrganizationUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class OrganizationUserResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationUserResource.class);

    @Inject
    OrganizationUserRepository organizationUserRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    @Inject
    private OrganizationService organizationService;

    @Inject
    private ServletContext servletContext;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private OrganizationRepository organizationRepository;


    /**
     * GET  /organization-users
     */
    @RequestMapping(value = "/organization-user/{organizationId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getOrganizationUsers(@PathVariable Long organizationId) {
        log.debug("REST request to get Organization Users : {}", organizationId);
        return organizationUserRepository.findOrganizationUsers(organizationId);
    }

    /**
     * GET  organization-user
     */
    @RequestMapping(value = "/organization-user/{organizationId}/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrganizationUserDTO> getOrganizationUser(@PathVariable Long organizationId, @PathVariable Long id) {
        log.debug("REST request to get Organization User : Organization ID {}, User ID {} ", organizationId, id);
        return Optional.ofNullable(organizationUserRepository.findOrganizationUser(id,organizationId)).
            map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    /**
     * POST  /organization-user
     */
    @RequestMapping(value = "/organization-user",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> addOrgUser(@RequestBody OrganizationUserDTO organizationUser, HttpServletRequest request,
                                                   HttpServletResponse response) {
        log.debug("REST request to add user to an organization, {}", organizationUser.getEmail());

        if(organizationUser.getUserId() == 0) {
            //*** Add User Button ***
            User user = userRepository.findByOneByLoginOrEmail(organizationUser.getEmail());
            if (user == null) {
                //1) Create New User
                user = userService.createUserInformation(
                    organizationUser.getEmail().toLowerCase(),
                    "",
                    organizationUser.getFirstName(),
                    organizationUser.getLastName(),
                    organizationUser.getEmail().toLowerCase(),
                    "en");

                log.debug("User Detail create success");
                organizationUser.setUserId(user.getId());
                final Locale locale = Locale.forLanguageTag(user.getLangKey());
                String content = createHtmlContentFromTemplate(user, locale, request, response);
                mailService.sendActivationEmail(user.getEmail(), content, locale);

            } else{
                organizationUser.setUserId(user.getId());
            }
            //2) Add to Organization
            if (organizationUserRepository.findOrganizationUser(organizationUser.getUserId(),
                organizationUser.getOrganizationId())==null){

                organizationUserRepository.create(organizationUser.getUserId(),
                    organizationUser.getOrganizationId());
            }

        }
        //3) Organization Role Update
       organizationService.updateOrganizationPermission(organizationUser);

        JsonResponse json = new JsonResponse();
        json.setSuccess(true);
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.CREATED);
    }

    private String createHtmlContentFromTemplate(final User user, final Locale locale, final HttpServletRequest request,
                                                 final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        variables.put("baseUrl", request.getScheme() + "://" +   // "http" + "://
            request.getServerName() +       // "myhost"
            ":" + request.getServerPort());
        IWebContext context = new SpringWebContext(request, response, servletContext,
            locale, variables, applicationContext);
        return templateEngine.process("activationEmail", context);
    }

}
