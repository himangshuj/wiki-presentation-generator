(ns presentation-generator.wiki-presentation
  (:require [clojure.test :refer :all ]
            [presentation-generator.convertor.wiki-presentation :as wp]))


(def sample-raw {:summary "sumary"
                 :slides [{:title "Brilliant life, tragic death", :summary
                 ". p. 1. Aaron Hillel Swartz was not depressed or suicidal … a rabbi’s wife who has known him since he was a child says.… At age 13 he won the ArsDigita Prize, a competition for young people who create noncommercial websites….",
                           :image "http://chicagojewishnews.com/wp-content/uploads/2014/01/photo_for_feature_3.jpg", :id 1}
                          {:title "‘Repairing the world’ was Aaron Swartz’s calling",
                           :summary " (Tel Aviv). … [H]is father said.… [A]lthough the young technologist and activist grew up to call himself an atheist, the values he grew up with appeared foundational.",
                           :image "http://www.haaretz.com/polopoly_fs/1.577627.1393853586!/image/4081846878.jpg_gen/derivatives/landscape_95/4081846878.jpg", :id 2} {:title "RSS creator Aaron Swartz dead at 26", :summary ". January 14, 2013. Swartz helped create RSS—a family of Web feed formats used to publish frequently updated works (blog entries, news headlines, …) in a standardized format—at the age of 14.", :image "http://harvardmagazine.com/sites/default/files/imagecache/scale_600x450/img/article/0113/Aaron_Swartz_wiki.jpg", :id 3} {:title "Remembering Aaron Swartz", :summary ". Aaron was one of the early architects of Creative Commons. As a teenager, he helped design the code layer to our licenses…", :image "http://creativecommons.org/wp-content/uploads/2013/01/lessig_swartz_600.jpg", :id 4} {:title "Internet activist charged with hacking into MIT network", :summary ". Arlington, Va.: Public Broadcasting Service. [Swartz] was in the middle of a fellowship at Harvard's Edmond J. Safra Center for Ethics, in its Lab on Institutional Corruption", :image "http://www-tc.pbs.org/wnet/need-to-know/files/2011/07/AaronSwartz.jpg", :id 8} {:title "MIT also pressing charges against hacking suspect", :summary ". [Swartz's] alleged use of MIT facilities and Web connections to access the JSTOR database ... resulted in two state felony charges for breaking into a ‘depository' and breaking & entering in the daytime, according to local prosecutors.", :image "http://images.politico.com/global/v3/politico44_nov4promo.jpg", :id 10} {:title "Aaron Swartz, internet freedom activist, dies aged 26", :summary ".", :image "http://news.bbcimg.co.uk/media/images/65244000/jpg/_65244524_65244523.jpg", :id 14} {:title "Pillars of Python: Web.py Web framework", :summary ".", :image "http://www.infoworld.com/sites/all/themes/ifw/images/add_resources/it_involve.jpg", :id 28} {:title "Feds Charge Activist as Hacker for Downloading Millions of Academic Articles", :summary ".", :image "http://www.wired.com/images_blogs/threatlevel/2011/07/2813084212_d3dd46ed5f_o-150x150.jpg", :id 31} {:title "Life inside the Aaron Swartz investigation", :summary ".", :image "http://cdn.theatlantic.com/static/mt/assets/science/quinnaaron_242.jpg", :id 36}]})


(deftest test-wiki-conversion
  (testing "Aaron conversion"
    (is (= 1 (wp/get-presentation-from-wiki-data sample-raw)))))