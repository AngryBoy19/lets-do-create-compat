# Publishing To Gitea

This repository is intended to live in:

```text
https://gitea.home.arpa/minecraft-mods/lets-do-create-compat
```

The source repository should contain code, docs, Gradle files, and workflow files. Built `.jar` files belong on Gitea Releases, not in the git source tree.

## Owner Layout

- `minecraft-mods`: Minecraft mod projects and compatibility add-ons.
- `sysadmin`: existing homelab restore/config repositories. These are left in place so current Wiki.js runbooks and clone URLs continue to work.

## First Release

Create a Gitea release:

- Tag: `v1.0.0`
- Title: `Let's Do Create Compat 1.0.0`
- Asset: `lets_do_create_compat-1.0.0-neoforge-1.21.1.jar`

Use `RELEASE_NOTES_v1.0.0.md` for the release notes.
