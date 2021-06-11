/**
 * ApperyFlow runner.
 */

@Grab('info.picocli:picocli-groovy:4.6.1')
import static picocli.CommandLine.*
import groovy.transform.Field

@Command(name = 'apperyflow', mixinStandardHelpOptions = true, version = '1.0',
  description = 'Building flow diagram from Appery Ionic5 project backup')
@picocli.groovy.PicocliScript

@Parameters(index = '0', description = 'Unzipped Appery Ionic5 project backup.')
@Field String backupFolder

@Option(names = [ "-s", "--start" ], arity = "0..1",
        description = "Start page.")
@Field String startPage;

println "-"*80
println "Backup folder: " + backupFolder

apperyFlow = new ApperyFlow()
apperyFlow.processBackupFolder(backupFolder, startPage)