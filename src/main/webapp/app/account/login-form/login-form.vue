<template>
  <div class="modal-body">
    <div class="row justify-content-center">
      <div class="col-md-10 km-margin-bottom">
        <b-alert
          show
          data-cy="loginError"
          variant="danger"
          v-if="authenticationError"
          v-html="t$('login.messages.error.authentication')"
        ></b-alert>
      </div>
      <div class="logo-login km-margin-bottom">
        <span class="logo-login-img"></span>
        <span v-text="t$('global.title')"></span>
      </div>
      <div class="col-md-10">
        <b-form v-on:submit.prevent="doLogin()">
          <b-form-group v-bind:label="t$('global.form[\'username.label\']')" label-for="username">
            <b-form-input
              id="username"
              type="text"
              name="username"
              autofocus
              v-bind:placeholder="t$('global.form[\'username.placeholder\']')"
              v-model="login"
              data-cy="username"
            >
            </b-form-input>
          </b-form-group>
          <b-form-group v-bind:label="t$('login.form.password')" label-for="password">
            <b-form-input
              id="password"
              type="password"
              name="password"
              v-bind:placeholder="t$('login.form[\'password.placeholder\']')"
              v-model="password"
              data-cy="password"
            >
            </b-form-input>
          </b-form-group>
          <div class="d-flex justify-content-between km-margin-bottom">
            <div>
              <b-form-checkbox id="rememberMe" name="rememberMe" v-model="rememberMe" checked>
                <span v-text="t$('login.form.rememberme')"></span>
              </b-form-checkbox>
            </div>
            <div>
              <b-link
                :to="'/account/reset/request'"
                class="alert-link"
                v-text="t$('login.password.forgot')"
                data-cy="forgetYourPasswordSelector"
              ></b-link>
            </div>
          </div>
          <div class="d-flex justify-content-end km-margin-bottom">
            <b-button
              :to="'/register'"
              type="submit"
              variant="outline-primary"
              class="btn-lg mr-3"
              v-text="t$('global.menu.account.register')"
            ></b-button>
            <b-button data-cy="submit" type="submit" variant="primary" class="btn-lg" v-text="t$('global.menu.account.login')"></b-button>
          </div>
        </b-form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./login-form.component.ts"></script>
