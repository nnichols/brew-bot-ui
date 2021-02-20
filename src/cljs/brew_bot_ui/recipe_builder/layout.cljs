(ns brew-bot-ui.recipe-builder.layout
  (:require [brew-bot-ui.shared.components.inputs :as inputs]
            [brew-bot-ui.shared.components.util :as cu]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn recipe-facts
  []
  (let [recipe-name   (r/atom nil)
        recipe-author (r/atom nil)]
    (fn []
      [:div {:class (cu/join-classes "rounded-lg" "bg-gray-100" "divide-y" "divide-gray-400" "sm:w-full" "md:w-full" "lg:w-2/3" "xl:w-2/3")
             :style {:padding   "5px"
                     :margin    "auto"
                     :min-width "300px"}
             :id    "RecipeFactsBox"}
       [:div {:class (cu/join-classes "text-center" "py-2")
              :id    "RecipeFactsHeader"}
        [:h3 "Recipe Builder"]]
       [:div {:id    "RecipeIdentifiers"
              :class (cu/join-classes "flex" "flex-wrap" "flex-row" "px-2" "py-2" "justify-around" "text-base")}
        [inputs/text {:style     {:min-width "140px"}
                      :value     @recipe-name
                      :id        "RecipeNameInputBox"
                      :label     "Recipe Name"
                      :on-change #(reset! recipe-name (-> % .-target .-value))}]
        [inputs/text {:style        {:min-width "140px"}
                      :value        @recipe-author
                      :id           "RecipeAuthorInputBox"
                      :label        "Recipe Author"
                      :autoComplete "name"
                      :on-change    #(reset! recipe-author (-> % .-target .-value))}]]
       [:div {:id    "RecipeScaling"
              :class (cu/join-classes "flex" "flex-wrap" "flex-row" "px-2" "justify-around" "text-base")}
        [:p "Units Toggle"]
        [:p "Brewing Method"]
        [:p "Fermentation Volume"]
        [:p "Boil Volume"]
        [:p "Boil Time"]
        [:p "Extraction Efficiency"]
        [:p "Target Style"]]
       [:div {:id    "RecipeStatistics"
              :class (cu/join-classes "flex" "flex-wrap" "flex-row" "px-2" "justify-around" "text-base" "divide-x" "divide-gray-400")}
        [:p "IBU"]
        [:p "OG"]
        [:p "FG"]
        [:p "ABV"]
        [:p "Color"]
        [:p "Projected Style"]]])))

(defn ingredient-box
  [_ingredient-type]
  (let [search-val (r/atom nil)]
    (fn [ingredient-type]
      [:div {:class (cu/join-classes "rounded-lg" "bg-gray-100" "divide-y" "divide-gray-400" "px-2" "sm:w-full" "md:w-full" "lg:w-5/12" "xl:w-5/12" "sm:my-4" "md:my-2" "lg:m-2" "xl:m-4")
             :style {:padding "5px" :min-width "300px"}}
       [:div {:class (cu/join-classes "text-center" "py-2")}
        [:h3 ingredient-type]]
       [:div {:class (cu/join-classes "text-base" "py-2")}
        [:p "This is the body"]
        [inputs/text {:value       @search-val
                      :id          "Sample Text Input"
                      :label       "Text"
                      :placeholder "This is a basic text input"
                      :on-change   #(reset! search-val (-> % .-target .-value))}]]
       [:div {:class (cu/join-classes "text-sm" "py-2")}
        [:p "This is the footer"]]])))

(defn main-panel
  []
  (fn []
    [:div {:class (cu/join-classes "justify-center")
           :style {:width "95%"}}
     [recipe-facts]
     [:div {:class (cu/join-classes "flex" "flex-wrap" "sm:flex-col" "md:flex-col" "lg:flex-row" "xl:flex-row" "justify-center")}
      [ingredient-box "Fermentables"]
      [ingredient-box "Hops"]
      [ingredient-box "Yeasts"]
      [ingredient-box "Miscellaneous Ingredients"]]]))
