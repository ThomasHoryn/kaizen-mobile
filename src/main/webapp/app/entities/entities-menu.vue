<template>
  <div>
    <b-dropdown-item to="/app-user">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.appUser')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/app-stats">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.appStats')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
