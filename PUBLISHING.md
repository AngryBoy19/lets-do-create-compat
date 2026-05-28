# Publishing To GitHub

This is the simple path if you are new to GitHub.

## 1. Create The Repository

1. Sign in to GitHub.
2. Click **New repository**.
3. Repository name: `lets-do-create-compat`.
4. Description: `Create automation compatibility for [Let's Do] Vinery.`
5. Choose **Public** if you want others to download and inspect the mod.
6. Do not add a README, license, or gitignore on GitHub. This project already has them.

## 2. Upload The Source Files

Upload these files and folders to the repository:

- `.github/`
- `docs/`
- `gradle/`
- `src/`
- `.gitattributes`
- `.gitignore`
- `build.gradle`
- `CHANGELOG.md`
- `gradle.properties`
- `gradlew`
- `gradlew.bat`
- `LICENSE`
- `PUBLISHING.md`
- `README.md`
- `RELEASE_NOTES_v1.0.0.md`
- `settings.gradle`

Do not upload these local folders or files:

- `.gradle/`
- `build/`
- `runs/`
- `create-src/`
- `vinery-src/`
- any `.jar` file to the source tree

The `.jar` file belongs on a GitHub Release, not inside the source repository.

## 3. Make The First Commit

If GitHub asks for a commit message, use:

```text
Initial 1.0.0 release
```

## 4. Check The Build

After upload, open the **Actions** tab in GitHub. The `Build` workflow should start automatically.

If it passes, GitHub will show a green check mark. The workflow also creates a downloadable build artifact.

## 5. Create A Release

1. Open the repository's **Releases** page.
2. Click **Draft a new release**.
3. Click **Choose a tag** and type `v1.0.0`.
4. Release title: `Let's Do Create Compat 1.0.0`.
5. Paste the contents of `RELEASE_NOTES_v1.0.0.md` into the release description.
6. Upload the built jar:

```text
lets_do_create_compat-1.0.0-neoforge-1.21.1.jar
```

7. Click **Publish release**.

## 6. Future Updates

For future versions:

1. Update `mod_version` in `gradle.properties`.
2. Add a new section to `CHANGELOG.md`.
3. Build the jar.
4. Create a new GitHub release with a matching tag, such as `v1.0.1`.
