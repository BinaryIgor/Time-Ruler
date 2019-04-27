package com.iprogrammerr.time.ruler.respondent.authentication;

import com.iprogrammerr.time.ruler.email.Emails;
import com.iprogrammerr.time.ruler.model.Hashing;
import com.iprogrammerr.time.ruler.model.user.Users;
import com.iprogrammerr.time.ruler.respondent.Respondent;
import com.iprogrammerr.time.ruler.validation.ValidateableEmail;
import com.iprogrammerr.time.ruler.validation.ValidateableName;
import com.iprogrammerr.time.ruler.validation.ValidateablePassword;
import com.iprogrammerr.time.ruler.view.ViewsTemplates;
import io.javalin.Context;
import io.javalin.Javalin;

import java.net.HttpURLConnection;

public class SigningUpRespondent implements Respondent {

    private static final String SIGN_UP = "sign-up";
    private static final String SIGN_UP_SUCCESS = "sign-up-success";
    private static final String SIGN_UP_FAILURE = "sign-up-failure";
    private static final String SIGN_IN = "sign-in";
    private static final String FORM_EMAIL = "email";
    private static final String FORM_LOGIN = "login";
    private static final String FORM_PASSWORD = "password";
    private static final String ACTIVATION = "activation";
    private final ViewsTemplates templates;
    private final Users users;
    private final Hashing hashing;
    private final Emails emails;

    public SigningUpRespondent(ViewsTemplates templates, Users users, Hashing hashing, Emails emails) {
        this.templates = templates;
        this.users = users;
        this.hashing = hashing;
        this.emails = emails;
    }

    @Override
    public void init(Javalin app) {
        app.get(SIGN_UP, ctx -> ctx.html(templates.rendered(SIGN_UP)));
        app.post(SIGN_UP, this::signUp);
    }

    //TODO style sign-up-success/failure view
    public void signUp(Context context) {
        ValidateableEmail email = new ValidateableEmail(context.formParam(FORM_EMAIL, ""));
        ValidateableName name = new ValidateableName(context.formParam(FORM_LOGIN, ""));
        ValidateablePassword password = new ValidateablePassword(context.formParam(FORM_PASSWORD, ""));
        if (email.isValid() && name.isValid() && password.isValid()) {
            createUser(email.value(), name.value(), password.value());
            context.html(SIGN_UP_SUCCESS);
            context.status(HttpURLConnection.HTTP_CREATED);
        } else {
            context.html(SIGN_UP_FAILURE);
            context.status(HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void createUser(String email, String name, String password) {
        long id = users.create(name, email, hashing.hash(password));
        emails.sendSignUpEmail(email, String.format("%s?%s=%s", SIGN_IN, ACTIVATION, userHash(email, name, id)));
    }

    private String userHash(String email, String name, long id) {
        return hashing.hash(email, name, String.valueOf(id));
    }
}