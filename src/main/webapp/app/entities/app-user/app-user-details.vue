<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="appUser">
        <h2 class="jh-entity-heading" data-cy="appUserDetailsHeading">
          <span v-text="t$('kaizenMobileApp.appUser.detail.title')"></span> {{ appUser.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('kaizenMobileApp.appUser.tenantId')"></span>
          </dt>
          <dd>
            <span>{{ appUser.tenantId }}</span>
          </dd>
          <dt>
            <span v-text="t$('kaizenMobileApp.appUser.internalUser')"></span>
          </dt>
          <dd>
            {{ appUser.internalUser ? appUser.internalUser.login : '' }}
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="appUser.id" :to="{ name: 'AppUserEdit', params: { appUserId: appUser.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./app-user-details.component.ts"></script>
