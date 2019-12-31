(ns brew-bot-ui.db
  (:require [brew-bot-ui.config :as config]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as cs]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers]
            [honeysql-postgres.format] ;must be required for the extension to work
            [honeysql-postgres.helpers :as pghelpers]
            [jdbc.pool.c3p0 :as pool]
            [nnichols.parse :as np])
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))

(def db-creds
  (when-let [user-info (.getUserInfo config/database-url)]
    (cs/split user-info #":")))

(def connection
  (let [username (first db-creds)
        password (second db-creds)
        db-host  (.getHost config/database-url)
        db-path  (.getPath config/database-url)
        subname  (if (= -1 (.getPort config/database-url))
                   (format "//%s%s" db-host db-path)
                   (format "//%s:%s%s" db-host (.getPort config/database-url) db-path))]
    (pool/make-datasource-spec
     {:classname   "org.postgresql.Driver"
      :subprotocol "postgresql"
      :user        username
      :password    password
      :subname     subname
      :ssl         true})))

(defn get-all-recipes
  []
  (let [q (sql/format (-> (helpers/select :*)
                          (helpers/from :beer_recipes)))
        result (jdbc/query connection q)]
    result))

(defn get-recipe-by-id
  [recipe-id]
  (let [recipe-uuid (np/parse-uuid recipe-id)
        q (sql/format (-> (helpers/select :*)
                          (helpers/from :beer_recipes)
                          (helpers/where [:= :recipe_id recipe-uuid])))
        result (jdbc/query connection q)]
    (first result)))
