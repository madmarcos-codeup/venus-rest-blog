export default function Home(props) {
    console.log("The frontend did it. HER FAULT");
    return `
        <header>
            <h1>Home Page</h1>
        </header>
        <main>
            <div>
<!--            <img src="img/IMG_0354.png">-->
            <a data-link href="/about"><i data-passthru class="fas fa-shopping-cart"></i></a>
            
            
                <form class="upload">
                    <div class="mb-3">
                        <label data-link for="flowerFile" class="form-label">Default file input example</label>
                        <input data-link  name="flower" class="form-control" type="file" id="flowerFile">
                        <input type="submit" id="uploadStuff">
                    </div>
                </form>
                
                
            </div>
        </main>
    `;
}

export function HomeEvents() {
    const API_KEY = '2b10AWm6xtO9e1Cy5HFKMgzIu'

    const uploadImg = document.querySelector(".upload");
    uploadImg.addEventListener("submit", async function (e) {
        e.preventDefault();
        console.log("Uploading stuff...");
    //
    //     // const image = '/Users/markrobinson/Desktop/image_1.jpeg';
        let file = e.target.flowerFile.files[0];
        // let reader = new FileReader();
        // reader.onload = uploadTheFile;
        // reader.readAsDataURL(file);

        let formData = new FormData();

        formData.append('organs', 'flower');
        formData.append('images', file);
        fetch(`https://my-api.plantnet.org/v2/identify/all?api-key=${API_KEY}`, {
            method: 'POST',
            body: formData
        })
            .then(res => res.json())
            .then(data => {
                if (data.errors) {
                    alert(data.errors)
                } else {
                    console.log(data);
                }
            })
    })
}
