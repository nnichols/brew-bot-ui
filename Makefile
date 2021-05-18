
MAKE = make
LEIN = lein

# These are the locations of the directories we'll use
TARGET_DIR = target
VERCHG = bin/verchg
TERRAFORM_DIR = deploy/terraform
VER = `sed -n -e "s/^.*defproject .* \"\(.*\)\"/\1/p" project.clj;`

#
# These are the main targets that we'll be making
#

tests/server:
	$(info Running server-side tests...)
	@ HEROKU_ENV=test $(LEIN) test

tests/app:
	$(info Running client-side tests...)
	@ HEROKU_ENV=test $(LEIN) doo once

tests/all:
	$(info Running server-side and client-side tests...)
	@ HEROKU_ENV=test $(LEIN) test-runner

artifacts:
	$(info Packaging production-like uberjar and client application...)
	@ NODE_ENV=production npm run css
	@ HEROKU_ENV=prod $(LEIN) prod-build

run/dev/tailwind:
	$(info Starting Tailwind Hot Reloading...)
	@ NODE_ENV=development npm run css:watch

run/dev/server:
	$(info Starting Clojure/Ring Hot Reloading...)
	@ HEROKU_ENV=dev $(LEIN) dev-server

run/dev/recipe-builder:
	$(info Starting Clojurescript Hot Reloading...)
	@ HEROKU_ENV=dev $(LEIN) recipe-dev-build

run/selenium:
	$(info Running the development environment for automated testing...)
	@ NODE_ENV=development npm run css
	@ HEROKU_ENV=test $(LEIN) selenium-build

run/prod:
	$(info Running the production environment locally...)
	@ NODE_ENV=production npm run css
	@ HEROKU_ENV=prod $(LEIN) prod-runner

version/major:
	$(info Updating major version and adding CHANGELOG entry...)
	@ $(VERCHG) 'major'
	@ NODE_ENV=production npm run css

version/minor:
	$(info Updating minor version and adding CHANGELOG entry...)
	@ $(VERCHG) 'minor'
	@ NODE_ENV=production npm run css

version/bugfix:
	$(info Updating bugfix version and adding CHANGELOG entry...)
	@ $(VERCHG) 'bugfix'
	@ NODE_ENV=production npm run css

version:
	$(info Adding CHANGELOG entry for existing version...)
	@ $(VERCHG) 'push'
	@ NODE_ENV=production npm run css

plan/prod:
	$(info Creating Terraform Plan...)
	@ $(MAKE) -C $(TERRAFORM_DIR) plan/prod

deploy/prod:
	$(info Deploying brew-bot-ui to Production...)
	@ $(MAKE) -C $(TERRAFORM_DIR) apply/prod

release/css:
	@ NODE_ENV=production npm run css
