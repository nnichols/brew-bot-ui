(ns brew-bot-ui.main
  "Application launcher for the web UI"
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.layout :as layout]
            [brew-bot-ui.routes :as routes]
            [day8.re-frame.http-fx]
            [district0x.re-frame.google-analytics-fx]
            [reagent.core :as r]
            [re-frame.core :as rf]

            ;; All Event/Sub namespaces must be included to load properly
            [brew-bot-ui.debug]
            [brew-bot-ui.events]
            [brew-bot-ui.subs]
            [brew-bot-ui.recipe-generation.events]
            [brew-bot-ui.recipe-generation.subs]
            [brew-bot-ui.recipes.subs]))

(enable-console-print!)

(defn mount-root
  "Render the main app panel, and mount it to the window"
  []
  (rf/clear-subscription-cache!)
  (r/render [#'layout/main-panel] (.getElementById js/document "app")))

(defn ^:export init
  "Initialize the app db, and mount the application.
  Exported to preserve fn name through advanced compilation name munging"
  []
  (district0x.re-frame.google-analytics-fx/set-enabled! config/google-analytics-on?)
  (rf/dispatch [:initialize-db])
  (mount-root))
