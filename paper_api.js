/**
 * Function to fetch all Paper versions, could be used to populate dropdown?
 * @returns {Promise<Array>} - Array of Paper versions
 */
async function fetchPaperVersions() {
    const url = 'https://api.papermc.io/v2/projects/paper';
    let arr = [];

    try {
      const response = await fetch(url);
  
      const data = await response.json();
      const versions = data.versions;
  
      versions.forEach(version => {
        arr.push(version);
      });
      return arr;
    } catch (error) {
      console.error(`Failed to retrieve versions: ${error}`);
    }
  }
let versions = await fetchPaperVersions();

let version = '1.16.5';

fetchBuildVersionUrl(version);

/**
 * Function to get download url for latest build of desired Paper version
 * Choose last build for latest version
 * @param {string} version - version to add to fetched url
*/
async function fetchBuildVersionUrl(version) {
    const versionUrl = `https://api.papermc.io/v2/projects/paper/versions/${version}`;

    try {
      const response = await fetch(versionUrl);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      const builds = data.builds;

      if (builds.length > 0) {
        const latestBuild = builds[builds.length - 1];
        const downloadUrl = `https://api.papermc.io/v2/projects/paper/versions/${version}/builds/${latestBuild}/downloads/paper-${version}-${latestBuild}.jar`;

        console.log(`Version ${version}: ${downloadUrl}`);
      } else {
        console.log(`Version ${version}: No builds available`);
      }
    } catch (error) {
      console.error(`Failed to retrieve builds for version ${version}: ${error}`);
    }
  }



  