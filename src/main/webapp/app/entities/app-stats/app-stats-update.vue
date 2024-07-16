<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate v-on:submit.prevent="save()">
        <h2
          id="kaizenMobileApp.appStats.home.createOrEditLabel"
          data-cy="AppStatsCreateUpdateHeading"
          v-text="t$('kaizenMobileApp.appStats.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="appStats.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="appStats.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('kaizenMobileApp.appStats.usedTenantId')" for="app-stats-usedTenantId"></label>
            <input
              type="text"
              class="form-control"
              name="usedTenantId"
              id="app-stats-usedTenantId"
              data-cy="usedTenantId"
              :class="{ valid: !v$.usedTenantId.$invalid, invalid: v$.usedTenantId.$invalid }"
              v-model="v$.usedTenantId.$model"
            />
            <div v-if="v$.usedTenantId.$anyDirty && v$.usedTenantId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.usedTenantId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./app-stats-update.component.ts"></script>
