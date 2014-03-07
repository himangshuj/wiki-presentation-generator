(ns presentation-generator.image-extractor
  (:require [clojure.test :refer :all ]
            [presentation-generator.scraper.image-extractor :as img-ext]))

(defn- url [val] (-> val first :url))
(defn- weight [val] (-> val first :weight))

(deftest checking-aron-www
  (testing "parsing AAron Swartz"
    (is (= "http://www.w3.org/Icons/w3c_home.gif" (url (img-ext/extract-images
                                                          [{:url "http://www.w3.org/2001/sw/RDFCore/members.html" }]
                                                          "In 2001, Swartz joined the RDFCore working group at the
                                                          World Wide Web Consortium (W3C)"))))
    (is (= "http://daringfireball.net/graphics/markdown/mt_textformat_menu.png"
          (url (img-ext/extract-images [{:url "http://daringfireball.net/projects/markdown/" }]
                     "In 2001, Swartz joined the RDFCore working group at the World Wide Web Consortium (W3C)"))))
    (is (= "http://www.infoworld.com/sites/all/themes/ifw/ifw_domain/images/application-development_left.png"
          (url (img-ext/extract-images [{:url "http://www.infoworld.com/d/application-development/pillars-python-webpy-web-framework-169072?page=0,0&1357985258=" }]
                  "Swartz attended Stanford University. After the summer of his freshman year, he attended Y Combinator's first Summer Founders
                   Program where he started the software company Infogami.
                   Infogami's wiki platform was used to support the Internet Archive's Open Library project and the web.py
                   web framework that Swartz had created,[28] but he felt he needed co-founders to proceed further.
                    Y-Combinator organizers suggested that Infogami merge with Reddit,[29][30] which it did in November 2005.
                    [29][31] Reddit at first found it difficult to make money from the project, but the site later gained in popularity,
                     with millions of users visiting it each month."))))

    (is (= 0
          (weight (img-ext/extract-images
                    [{:url "http://www.infoworld.com/d/application-development/pillars-python-webpy-web-framework-169072?page=0,0&1357985258=" }]
                     "Swartz attended Stanford University. After the summer of his freshman year, he attended Y Combinator's first Summer Founders
                      Program where he started the software company Infogami.
                      Infogami's wiki platform was used to support the Internet Archive's Open Library project and the web.py
                      web framework that Swartz had created,[28] but he felt he needed co-founders to proceed further.
                       Y-Combinator organizers suggested that Infogami merge with Reddit,[29][30] which it did in November 2005.
                       [29][31] Reddit at first found it difficult to make money from the project, but the site later gained in popularity,
                        with millions of users visiting it each month."))))
    (is (= "http://web.archive.org/web/20071224194042im_/http://files.infogami.com/infogami.com/infogamilogo.png"
          (url (img-ext/extract-images [{:url "http://web.archive.org/web/20071224194042/http://infogami.com/blog/introduction" }]
                  "Swartz attended Stanford University. After the summer of his freshman year, he attended Y Combinator's first Summer Founders
                   Program where he started the software company Infogami.
                   Infogami's wiki platform was used to support the Internet Archive's Open Library project and the web.py
                   web framework that Swartz had created,[28] but he felt he needed co-founders to proceed further.
                    Y-Combinator organizers suggested that Infogami merge with Reddit,[29][30] which it did in November 2005.
                    [29][31] Reddit at first found it difficult to make money from the project, but the site later gained in popularity,
                     with millions of users visiting it each month."))))
    (is (= -1
          (weight (img-ext/extract-images [{:url "http://web.archive.org/web/20071224194042/http://infogami.com/blog/introduction" }]
                     "Swartz attended Stanford University. After the summer of his freshman year, he attended Y Combinator's first Summer Founders
                      Program where he started the software company Infogami.
                      Infogami's wiki platform was used to support the Internet Archive's Open Library project and the web.py
                      web framework that Swartz had created,[28] but he felt he needed co-founders to proceed further.
                       Y-Combinator organizers suggested that Infogami merge with Reddit,[29][30] which it did in November 2005.
                       [29][31] Reddit at first found it difficult to make money from the project, but the site later gained in popularity,
                        with millions of users visiting it each month."))))
    (is (= nil
          (weight (img-ext/extract-images [{:url "http://techcrunch.com/2006/10/31/breaking-news-conde-nastwired-acquires-reddit/" }]
                     "Swartz attended Stanford University. After the summer of his freshman year, he attended Y Combinator's first Summer Founders
                      Program where he started the software company Infogami.
                      Infogami's wiki platform was used to support the Internet Archive's Open Library project and the web.py
                      web framework that Swartz had created,[28] but he felt he needed co-founders to proceed further.
                       Y-Combinator organizers suggested that Infogami merge with Reddit,[29][30] which it did in November 2005.
                       [29][31] Reddit at first found it difficult to make money from the project, but the site later gained in popularity,
                        with millions of users visiting it each month."))))
    ))
(deftest checking-aron-wiki
  (testing "parsing AAron Swartz"
    (is (= "//upload.wikimedia.org/wikipedia/en/a/a4/Sw-horz-w3c.png" (url (img-ext/extract-images [{:url "http://en.wikipedia.org/wiki/Semantic_Web" :type :wiki}]
                                                                              "Swartz was co-creator, with John Gruber, of Markdown,
                                                                               a simplified markup standard derived from HTML, and author of its html2text translator.
                                                                                Markdown remains in widespread use."))))
    (is (= -1
          (weight (img-ext/extract-images [{:url "http://en.wikipedia.org/wiki/Cond%C3%A9_Nast_Publications" :type :wiki}]
                     "In October 2006, Reddit was acquired by Condé Nast Publications, the owner of Wired magazine.[19][32] Swartz moved with his company to San Francisco to work on Wired.[19] Swartz found office life uncongenial, and he ultimately left the company"))))

    (is (= 0
          (weight (img-ext/extract-images [{:url "http://en.wikipedia.org/wiki/Wired_(magazine)" :type :wiki}]
                     "In October 2006, Reddit was acquired by Condé Nast Publications, the owner of Wired magazine.[19][32] Swartz moved with his company to San Francisco to work on Wired.[19] Swartz found office life uncongenial, and he ultimately left the company"))))

    (is (= "//upload.wikimedia.org/wikipedia/en/2/2d/Cover_of_Wired_issue_1.04_September_October_1993.jpg"
          (url (img-ext/extract-images [{:url "http://en.wikipedia.org/wiki/Wired_(magazine)" :type :wiki}]
                  "In October 2006, Reddit was acquired by Condé Nast Publications, the owner of Wired magazine.[19][32] Swartz moved with his company to San Francisco to work on Wired.[19] Swartz found office life uncongenial, and he ultimately left the company"))))


    (is (= "//upload.wikimedia.org/wikipedia/commons/a/a9/Leland_Stanford_p1070023.jpg"
          (url (img-ext/extract-images [{:url "http://en.wikipedia.org/wiki/Stanford_University" :type :wiki}]
                  "Swartz attended Stanford University.
                  After the summer of his freshman year, he attended Y Combinator's first Summer Founders Program
                   where he started the software company Infogami.
                   Infogami's wiki platform was used to support the Internet Archive's Open Library project
                    and the web.py web framework that Swartz had created but he felt he needed co-founders to proceed further. Y-Combinator organizers suggested that Infogami merge with Reddit,[29][30] which it did in November 2005.[29][31] Reddit at first found it difficult to make money from the project, but the site later gained in popularity, with millions of users visiting it each month."))))
    ))
(deftest check-aaron-proper-noun
  (testing "proper noun weights"
    (is (= 1 (img-ext/extract-images [{:url "http://www.theguardian.com/technology/2013/jan/13/aaron-swartz"}]
               "At age 13, Swartz won an ArsDigita Prize, given to young people who create useful, educational, and collaborative noncommercial websites. At age 14, he became a member of the working group that authored the RSS 1.0 web syndication specification.")))))