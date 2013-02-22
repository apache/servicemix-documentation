## Prerequisites ##
You need to have these two programs installed on your machine:

- [Pygments](http://pygments.org/) - syntax highlighting for HTML and PDF
- [Prince XML](http://www.princexml.com/) - converts HTML into PDF for the manual 

## Building the documentation project ##
There are two ways of building the documentation project:

- `mvn -Plive jetty:run` will make a live website copy available [locally](http://localhost:8080).  This is very convenient while editing the docs to quickly review the changes you made.
- `mvn clean install` will build the documentation project, creating 3 representations:
  - a war file containing the documentation webapp
  - a pdf file (in `target/sitegen/manual.pdf`)
  - a static website, generated using the same templates (in `target/sitegen`)
  
## Publishing the documentation to the website ##
If you're an Apache ServiceMix committer, you can publish a new copy of the documentation pages with this command:
`mvn clean install scm-publish:publish-scm`  
  
## Known issues ##

- The war file currently can't be deployed on ServiceMix


