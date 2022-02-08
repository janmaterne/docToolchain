//tag::globalConfig[]
// Path where all output will be done.
// Should be ignored by the version control system.
outputPath = 'build'

// Path where the docToolchain will search for the input files.
// This path is appended to the docDir property specified in gradle.properties
// or in the command line, and therefore must be relative to it.
inputPath = 'src/docs';

// Path where the PDF theme is placed.
// For theming see https://github.com/asciidoctor/asciidoctor-pdf/blob/main/docs/theming-guide.adoc
pdfThemeDir = './src/docs/pdfTheme'

// Specify the files to render and there formats.
//TODO: which formats are available?
inputFiles = [
        //[file: 'doctoolchain_demo.adoc',       formats: ['html','pdf']],
        //[file: 'arc42-template.adoc',    formats: ['html','pdf']],
        /** inputFiles **/
]

// Folders in which asciidoc will find images.
// These will be copied as resources to ./images
// Folders are relative to inputPath.
imageDirs = [
    /** imageDirs **/
]

// These are directories (dirs) and files which Gradle monitors for a change
// in order to decide if the docs have to be re-build
//TODO: recursive scanning or not?
taskInputsDirs = [
                    "${inputPath}",
//                  "${inputPath}/src",
//                  "${inputPath}/images",
                 ]

taskInputsFiles = []
//end::globalConfig[]

//*****************************************************************************************

//tag::micrositeConfig[]
//Configuration for microsite: generateSite + previewSite

microsite = [:]

// These properties will be set as jBake properties.
// microsite.foo will be site.foo in jBake and can be used as config.site_foo in a template.
// See https://jbake.org/docs/2.6.4/#configuration for how to configure jBake.
// Other properties listed here might be used in the jBake templates and thus are not
// documented in the jBake docs but hopefully in the template docs.
microsite.with {
    
    /** start:microsite **/

    // Is your microsite deployed with a context path?
    contextPath = '/'
    // Configure a port on which your preview server will run.
    previewPort = 8881
    // The folder of a site definition (theme) relative to the docDir+inputPath
    //siteFolder = '../site'

    /** end:microsite **/

    // project theme
    // site folder relative to the docs folder
    // see 'copyTheme' for more details
    siteFolder = '../site'

    // The title of the microsite, displayed in the upper left corner.
    title = '##site-title##'
    
    /** start:footer-configuration */

    // Contact eMail
    // example: mailto:bert@example.com
    footerMail = '##footer-email##'

    // Twitter account URL
    // example: http://twitter.com/bert
    footerTwitter = '##twitter-url##'
    
    // Stackoverflow QA
    // example: https://stackoverflow.com/questions/tagged/doctoolchain
    footerSO = '##Stackoverflow-url##'
    
    // Github Repository
    // example: https://github.com/docToolchain/docToolchain
    footerGithub = '##Github-url##'
    
    // Slack Channel
    // example: https://gradle-community.slack.com/
    footerSlack = '##Slack-url##'
    
    // Footer Text
    // example: <small class="text-white">built with docToolchain and jBake <br /> theme: docsy</small>
    footerText = '<small class="text-white">built with <a href="https://doctoolchain.org">docToolchain</a> and <a href="https://jbake.org">jBake</a> <br /> theme: <a href="https://www.docsy.dev/">docsy</a></small>'

    //TODO: How does footer* and footerText fit together?
    /** end:footer-configuration */

    // Site title if no other title is given.
    title = 'docToolchain'

    // The url to create an issue in Github
    // Example: https://github.com/docToolchain/docToolchain/issues/new
    issueUrl = '##issue-url##'

    // The base url for code files in github
    // Example: https://github.com/doctoolchain/doctoolchain/edit/master/src/docs
    branch = System.getenv("DTC_PROJECT_BRANCH")
    gitRepoUrl = '##git-repo-url##'
    //TODO: How fit branch and url together? The branch is part of the url ('master' in the example).

    // The location of the landing page.
    landingPage = 'landingpage.gsp'

    // The menu of the microsite. A map of [code:'title'] entries to specify the order and title of the entries.
    // The codes are autogenerated from the folder names or :jbake-menu: attribute entries from the .adoc file headers.
    // Set a title to '-' in order to remove this menu entry.
    menu = [:]
    
}
//end::micrositeConfig[]

//*****************************************************************************************

//tag::changelogConfig[]
// Configuration for exportChangelog

exportChangelog = [:]

changelog.with {

    // Directory of which the exportChangelog task will export the changelog.
    // It should be relative to the docDir directory provided in the
    // gradle.properties file.
    dir = 'src/docs'

    // Command used to fetch the list of changes.
    // It should be a single command taking a directory as a parameter.
    // You cannot use multiple commands with pipe between.
    // This command will be executed in the directory specified by changelogDir
    // and its environment is inherited from the parent process.
    // This command should produce asciidoc text directly to STDOUT. 
    // The exportChangelog task does not do any post-processing
    // of the output of that command.
    //
    // See also https://git-scm.com/docs/pretty-formats
    cmd = 'git log --pretty=format:%x7c%x20%ad%x20%n%x7c%x20%an%x20%n%x7c%x20%s%x20%n --date=short'

}
//end::changelogConfig[]

//*****************************************************************************************

//tag::confluenceConfig[]
// Configuration for publishToConfluence

confluence = [:]

// 'input' is an array of files to upload to Confluence with the ability
//  to configure a different parent page for each file.
//
// Attributes per entry:
// - 'file': absolute or relative path to the asciidoc generated html file to be exported ('file' or 'url' must be specified)
// - 'url': absolute URL to an asciidoc generated html file to be exported ('file' or 'url' must be specified)
// - 'ancestorName' (optional): the name of the parent page in Confluence as string;
//                            this attribute has priority over ancestorId, but if page with given name doesn't exist,
//                            ancestorId will be used as a fallback
// - 'ancestorId' (optional): the id of the parent page in Confluence as string; leave this empty
//                            if a new parent shall be created in the space
// - 'preambleTitle' (optional): the title of the page containing the preamble (everything
//                            before the first second level heading). Default is 'arc42'
//
// The following four keys can also be used in the global section below
// - 'spaceKey' (optional): page specific variable for the key of the confluence space to write to
// - 'createSubpages' (optional): page specific variable to determine whether ".sect2" sections shall be split from the current page into subpages
// - 'pagePrefix' (optional): page specific variable, the pagePrefix will be a prefix for the page title and it's sub-pages
//                            use this if you only have access to one confluence space but need to store several
//                            pages with the same title - a different pagePrefix will make them unique
// - 'pageSuffix' (optional): same usage as prefix but appended to the title and it's subpages
//
// Only 'file' or 'url' is allowed. If both are given, 'url' is ignored.
confluence.with {
    input = [
        [ file: "build/html5/arc42-template-de.html" ],
        [ file: "build/html5/arc42-template-en.html" ]
        // [ url: "http://server:8080/mypage.html", ancestorName: "My Page" ]
    ]

    // Endpoint of the confluenceAPI (REST) to be used.
    // Verify that you got the correct endpoint by browsing to
    // https://[yourServer]/[context]/rest/api/user/current
    // You should get a valid json which describes your current user.
    // A working example is https://arc42-template.atlassian.net/wiki/rest/api/user/current
    api = 'https://[yourServer]/[context]/rest/api/'

    // Additionally, spaceKey, createSubpages, pagePrefix and pageSuffix can be globally defined here. The assignment in the input array has precedence.

    // The key of the confluence space to write to.
    spaceKey = 'asciidoc'

    // The title of the page containing the preamble (everything the first second level heading). Default is 'arc42'.
    preambleTitle = ''

    // Variable to determine whether ".sect2" sections shall be split from the current page into subpages.
    createSubpages = false

    // The pagePrefix will be a prefix for each page title.
    // Use this if you only have access to one confluence space but need to store several
    // pages with the same title - a different pagePrefix will make them unique.
    pagePrefix = ''

    // The pageSuffix - analog to pagePrefix.
    pageSuffix = ''

    /*
    WARNING: It is strongly recommended to store credentials securely instead of commiting plain text values to your git repository!!!

    Tool expects credentials that belong to an account which has the right permissions to create and edit confluence pages in the given space.
    Credentials can be used in a form of:
     - passed parameters when calling script (-PconfluenceUser=myUsername -PconfluencePass=myPassword) which can be fetched as a secrets on CI/CD or
     - gradle variables set through gradle properties (uses the 'confluenceUser' and 'confluencePass' keys)
    Often, same credentials are used for Jira & Confluence, in which case it is recommended to pass CLI parameters for both entities as
    -Pusername=myUser -Ppassword=myPassword
    */

    // Optional API-token to be added in case the credentials are needed for user and password exchange.
    //apikey = "[API-token]"

    // HTML Content that will be included with every page published
    // directly after the TOC. If left empty no additional content will be
    // added
    // extraPageContent = '<ac:structured-macro ac:name="warning"><ac:parameter ac:name="title" /><ac:rich-text-body>This is a generated page, do not edit!</ac:rich-text-body></ac:structured-macro>
    extraPageContent = ''

    // enable or disable attachment uploads for local file references
    enableAttachments = false

    // Default attachmentPrefix = attachment - All files to attach will require to be linked inside the document.
    // attachmentPrefix = "attachment"

    // Optional proxy configuration, only used to access Confluence.
    // Schema supports http and https.
    // proxy = [host: 'my.proxy.com', port: 1234, schema: 'http']

    // Optional: specify which Confluence OpenAPI Macro should be used to render OpenAPI definitions.
    // Possible values: ["confluence-open-api", "open-api", true]. true is the same as "confluence-open-api" for backward compatibility
    // useOpenapiMacro = "confluence-open-api"
}
//end::confluenceConfig[]

//*****************************************************************************************

//tag::exportEAConfig[]
// Configuration for the export script 'exportEA.vbs'.
// The following parameters can be used to change the default behaviour of 'exportEA'.
// All parameter are optional.
// Parameter 'connection' allows to select a certain database connection by using the ConnectionString as used for
// directly connecting to the project database instead of looking for EAP/EAPX files inside and below the 'src' folder.
// Parameter 'packageFilter' is an array of package GUID's to be used for export. All images inside and in all packages below the package represented by its GUID are exported.
// A packageGUID, that is not found in the currently opened project, is silently skipped.
// PackageGUID of multiple project files can be mixed in case multiple projects have to be opened.

exportEA.with {
// OPTIONAL: Set the connection to a certain project or comment it out to use all project files inside the src folder or its child folder.
// connection = "DBType=1;Connect=Provider=SQLOLEDB.1;Integrated Security=SSPI;Persist Security Info=False;Initial Catalog=[THE_DB_NAME_OF_THE_PROJECT];Data Source=[server_hosting_database.com];LazyLoad=1;"
// OPTIONAL: Add one or multiple packageGUIDs to be used for export. All packages are analysed, if no packageFilter is set.
// packageFilter = [
//                    "{A237ECDE-5419-4d47-AECC-B836999E7AE0}",
//                    "{B73FA2FB-267D-4bcd-3D37-5014AD8806D6}"
//                  ]
// OPTIONAL: relative path to base 'docDir' to which the diagrams and notes are to be exported
// exportPath = "src/docs/"
// OPTIONAL: relative path to base 'docDir', in which Enterprise Architect project files are searched
// searchPath = "src/docs/"

}
//end::exportEAConfig[]

//*****************************************************************************************

//tag::htmlSanityCheckConfig[]
htmlSanityCheck.with {
    //sourceDir = "build/html5/site"
    //checkingResultsDir =
}
//end::htmlSanityCheckConfig[]

//*****************************************************************************************

//tag::jiraConfig[]
// Configuration for Jira related tasks

jira = [:]

jira.with {

    // Endpoint of the JiraAPI (REST) to be used.
    api = 'https://your-jira-instance'

    /*
    WARNING: It is strongly recommended to store credentials securely instead of commiting plain text values to your git repository!!!

    Tool expects credentials that belong to an account which has the right permissions to read the JIRA issues for a given project.
    Credentials can be used in a form of:
     - passed parameters when calling script (-PjiraUser=myUsername -PjiraPass=myPassword) which can be fetched as a secrets on CI/CD or
     - gradle variables set through gradle properties (uses the 'jiraUser' and 'jiraPass' keys)
    Often, Jira & Confluence credentials are the same, in which case it is recommended to pass CLI parameters for both entities as
    -Pusername=myUser -Ppassword=myPassword
    */

    // The key of the Jira project.
    project = 'PROJECTKEY'

    // The format of the received date time values to parse.
    dateTimeFormatParse = "yyyy-MM-dd'T'H:m:s.SSSz" // i.e. 2020-07-24'T'9:12:40.999 CEST

    // The format in which the date time should be saved to output.
    dateTimeFormatOutput = "dd.MM.yyyy HH:mm:ss z" // i.e. 24.07.2020 09:02:40 CEST

    // The label to restrict search to.
    //TODO: really unset? Or '' or commented out?
    label =

    // Legacy settings for Jira query. This setting is deprecated & support for it will soon be completely removed. Please use JiraRequests settings
    //jql = "project='%jiraProject%' AND labels='%jiraLabel%' ORDER BY priority DESC, duedate ASC"

    // Base filename in which Jira query results should be stored
    resultsFilename = 'JiraTicketsContent'

    saveAsciidoc = true // if true, asciidoc file will be created with *.adoc extension
    saveExcel = true // if true, Excel file will be created with *.xlsx extension

    // Output folder for this task inside main outputPath
    resultsFolder = 'JiraRequests'

    /*
    List of requests to Jira API:
    These are basically JQL expressions bundled with a filename in which results will be saved.
    User can configure custom fields IDs and name those for column header,
    i.e. customfield_10026:'Story Points' for Jira instance that has custom field with that name and will be saved in a coloumn named "Story Points"
    */
    requests = [
        new JiraRequest(
            filename:"File1_Done_issues",
            jql:"project='%jiraProject%' AND status='Done' ORDER BY duedate ASC",
            customfields: [customfield_10026:'Story Points']
        ),
        new JiraRequest(
            filename:'CurrentSprint',
            jql:"project='%jiraProject%' AND Sprint in openSprints() ORDER BY priority DESC, duedate ASC",
            customfields: [customfield_10026:'Story Points']
        ),
    ]
}

@groovy.transform.Immutable
class JiraRequest {
    String filename  //filename (without extension) of the file in which JQL results will be saved. Extension will be determined automatically for Asciidoc or Excel file
    String jql // Jira Query Language syntax
    Map<String,String> customfields // map of customFieldId:displayName values for Jira fields which don't have default names, i.e. customfield_10026:StoryPoints
}
//end::jiraConfig[]

//*****************************************************************************************

//tag::openApiConfig[]
// Configuration for OpenAPI related task

openApi = [:]

// 'specFile' is the name of OpenAPI specification yaml file. Tool expects this file inside working dir (as a filename or relative path with filename).
// 'infoUrl' and 'infoEmail' are specification metadata about further info related to the API. By default this values would be filled by openapi-generator plugin placeholders.

openApi.with {
    specFile = 'src/docs/petstore-v2.0.yaml' // i.e. 'petstore.yaml', 'src/doc/petstore.yaml'
    infoUrl = 'https://my-api.company.com'
    infoEmail = 'info@company.com'
}
//end::openApiConfig[]

//*****************************************************************************************

//tag::sprintChangelogConfig[]
// Sprint changelog configuration generate changelog lists based on tickets in sprints of an Jira instance.
// This feature requires at least Jira API & credentials to be properly set in Jira section of this configuration.

sprintChangelog = [:]

sprintChangelog.with {
    sprintState = 'closed' // it is possible to define multiple states, i.e. 'closed, active, future'
    ticketStatus = "Done, Closed" // it is possible to define multiple ticket statuses, i.e. "Done, Closed, 'in Progress'"

    showAssignee = false
    showTicketStatus = false
    showTicketType = true
    sprintBoardId = 12345  // Jira instance probably have multiple boards; here it can be defined which board should be used

    // Output folder for this task inside main outputPath.
    resultsFolder = 'Sprints'

    // If sprintName is not defined or sprint with that name isn't found, release notes will be created on for all sprints that match sprint state configuration.
    sprintName = 'PRJ Sprint 1' // If sprint with a given sprintName is found, release notes will be created just for that sprint.
    allSprintsFilename = 'Sprints_Changelogs' // Extension will be automatically added.
}
//end::sprintChangelogConfig[]
