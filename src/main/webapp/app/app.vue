<template>
  <div id="app" class="mk-bg-light">
    <ribbon></ribbon>
    <div id="app-header">
      <jhi-navbar></jhi-navbar>
    </div>
    <div class="container-fluid mk-bg-light">
      <div class="card jh-card mk-bg-light">
        <router-view></router-view>
      </div>
      <b-modal id="login-page" hide-footer lazy size="lg">
        <template #modal-title>
          <h2 data-cy="loginTitle" class="text-center" v-text="t$('login.title')"></h2>
        </template>
        <login-form></login-form>
      </b-modal>

      <jhi-footer></jhi-footer>
    </div>
  </div>
</template>

<script lang="ts" src="./app.component.ts"></script>
