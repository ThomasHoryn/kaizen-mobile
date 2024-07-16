<template>
  <div>
    <h2 id="page-heading" data-cy="AppStatsHeading">
      <span v-text="t$('kaizenMobileApp.appStats.home.title')" id="app-stats-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('kaizenMobileApp.appStats.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AppStatsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-app-stats"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('kaizenMobileApp.appStats.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && appStats && appStats.length === 0">
      <span v-text="t$('kaizenMobileApp.appStats.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="appStats && appStats.length > 0">
      <table class="table table-striped" aria-describedby="appStats">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('kaizenMobileApp.appStats.usedTenantId')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appStats in appStats" :key="appStats.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AppStatsView', params: { appStatsId: appStats.id } }">{{ appStats.id }}</router-link>
            </td>
            <td>{{ appStats.usedTenantId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AppStatsView', params: { appStatsId: appStats.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AppStatsEdit', params: { appStatsId: appStats.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(appStats)"
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
        <span id="kaizenMobileApp.appStats.delete.question" data-cy="appStatsDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-appStats-heading" v-text="t$('kaizenMobileApp.appStats.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-appStats"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeAppStats()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./app-stats.component.ts"></script>
