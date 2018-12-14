# arealle
This project is experimental. It was designed to model physical real estate plots and features within their proximity. For example, it can be determined that plot X, on street Y, is within close proximity to certain amenities such as a beach, conservation area, or public transportation.

Data was imported from generally available MassGIS ESRI datasets, including plot plans and various features sets for bodies of water, conservation areas, public transportation etc.

Processes were developed to parse the datasets, load them into a combination of relational and non-relational stores as a basis for a service APIs used to support a front-end.

Back-end data is consumed via several dedicated tasks that first break data down into relational records, which are then fed into a Mongo database for storing property documents (eg. a plot at a street address with various dwelling attributes), and a SOLR store used solely to store GEO coordinates for properties and amenities, and to perform proximity calculations between them.

The UI, built on the data service, is a single-page application developed primarily using the Backbone and Marionette JavaScript libraries.
