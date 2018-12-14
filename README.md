# arealle
This project is an experimental effort. Generally, it was designed to model physical real estate plots and features within their proximity. For example, it can be determined that plot X, on street Y, is within close proximity to certain amenities such as a beach, conservation area, or public transportation.

Data was imported from generally available MassGIS datasets, including plot plans and various features sets for bodies of water, conservation areas, public transportation etc.

Processes were developed to parse out the ESRI datasets, load them into a combination of relational and non-relational stores as a basis for services APIs that are then used to support a front-end.

Back-end data is consumed via several dedicated tasks that first break data down into relational records, which are then fed into a Mongo database which contains property documents (eg. a house at an address with various attributes), and a SOLR store which is used to manage GEO coordinates for properties in the Mongo store.

On top of these data stores is a service which is used to support a single-page application developed in JavaScript libraries, primarily Backbone and Marionette.
