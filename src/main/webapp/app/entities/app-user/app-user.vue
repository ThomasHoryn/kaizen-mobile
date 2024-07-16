<template>
  <div>
    <h2 id="page-heading" data-cy="AppUserHeading">
      <span v-text="t$('kaizenMobileApp.appUser.home.title')" id="app-user-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('kaizenMobileApp.appUser.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AppUserCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-app-user"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('kaizenMobileApp.appUser.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && appUsers && appUsers.length === 0">
      <span v-text="t$('kaizenMobileApp.appUser.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="appUsers && appUsers.length > 0">
      <table class="table table-striped" aria-describedby="appUsers">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('kaizenMobileApp.appUser.tenantId')"></span></th>
            <th scope="row"><span v-text="t$('kaizenMobileApp.appUser.internalUser')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appUser in appUsers" :key="appUser.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AppUserView', params: { appUserId: appUser.id } }">{{ appUser.id }}</router-link>
            </td>
            <td>{{ appUser.tenantId }}</td>
            <td>
              {{ appUser.internalUser ? appUser.internalUser.login : '' }}
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AppUserView', params: { appUserId: appUser.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AppUserEdit', params: { appUserId: appUser.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(appUser)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="kaizenMobileApp.appUser.delete.question" data-cy="appUserDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-appUser-heading" v-text="t$('kaizenMobileApp.appUser.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-appUser"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeAppUser()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./app-user.component.ts"></script>
