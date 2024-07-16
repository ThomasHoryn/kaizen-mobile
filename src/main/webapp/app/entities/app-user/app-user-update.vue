<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate v-on:submit.prevent="save()">
        <h2
          id="kaizenMobileApp.appUser.home.createOrEditLabel"
          data-cy="AppUserCreateUpdateHeading"
          v-text="t$('kaizenMobileApp.appUser.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="appUser.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="appUser.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('kaizenMobileApp.appUser.tenantId')" for="app-user-tenantId"></label>
            <input
              type="text"
              class="form-control"
              name="tenantId"
              id="app-user-tenantId"
              data-cy="tenantId"
              :class="{ valid: !v$.tenantId.$invalid, invalid: v$.tenantId.$invalid }"
              v-model="v$.tenantId.$model"
            />
            <div v-if="v$.tenantId.$anyDirty && v$.tenantId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tenantId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('kaizenMobileApp.appUser.internalUser')" for="app-user-internalUser"></label>
            <select
              class="form-control"
              id="app-user-internalUser"
              data-cy="internalUser"
              name="internalUser"
              v-model="appUser.internalUser"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="appUser.internalUser && userOption.id === appUser.internalUser.id ? appUser.internalUser : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.login }}
              </option>
            </select>
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
<script lang="ts" src="./app-user-update.component.ts"></script>
