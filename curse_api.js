const API_KEY = process.env.API_KEY;
const basePoint = 'https://api.curseforge.com';

const headers = {
    'Accept':'application/json',
    'x-api-key':API_KEY
  };

const fetchData = async () => {
    try {
        const res = await fetch(`${basePoint}/v1/minecraft/version`, {
            method: 'GET',
            headers: headers
        });
        const body = await res.json();
        //console.log(body.data);
        //console.log(body);
    } catch (error) {
        console.error(error);
    }
};

  
await fetchModLoaders();

//await fetchData();

async function fetchModLoaders() {
    try {
        const res = await fetch(`${basePoint}/v1/minecraft/modloader/forge-9.10.0.841`, {
            method: 'GET',
            headers: headers
        });
        const body = await res.json();
        console.log(body.data.downloadUrl);
    } catch (error) {
        console.error(error);
    }
}
