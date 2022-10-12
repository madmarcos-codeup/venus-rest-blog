export default function Home(props) {
    console.log("The frontend did it. HER FAULT");
    return `
        <header>
            <h1>Home Page</h1>
        </header>
        <main>
            <div>
                <form class="upload" method="post" action="https://my-api.plantnet.org/v2/identify/all?api-key=2b10AWm6xtO9e1Cy5HFKMgzIu" enctype="multipart/form-data">
    <div class="mb-3">
  <label data-link for="formFile" class="form-label">Default file input example</label>
  <input data-link  name="images" class="form-control" type="file" id="formFile">
  <input type="submit" id="uploadStuff">
</div>
</form>
            </div>
        </main>
    `;
}

export function HomeEvents() {
    // const  API_KEY = '2b10AWm6xtO9e1Cy5HFKMgzIu'
    // const uploadImg = document.querySelector(".upload");
    // uploadImg.addEventListener("submit", function (e) {
    //     e.preventDefault();
    //     console.log("Uploading stuff...");
    //
    //     // const image = '/Users/markrobinson/Desktop/image_1.jpeg';
    //     let file = e.target.uploadFile.files[0]
    //     let form = new FormData()
    //     form.append('image', file)
    //     console.log(file)
    //
    //     fetch(`https://my-api.plantnet.org/v2/identify/all?include-related-images=false&no-reject=false&lang=en&api-key=2b10AWm6xtO9e1Cy5HFKMgzIu`, {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'multipart/form-data'
    //         },
    //         body: JSON.stringify(form)
    //     })
    //         .then(res => res.json())
    //         .then(data => {
    //             if (data.errors) {
    //                 alert(data.errors)
    //             } else {
    //                 console.log(data);
    //             }
    //         })
    // })
}