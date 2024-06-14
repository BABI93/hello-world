// Small wrapper around fetch()
// Adds responseType (json,text,...), get, post, postJson options.
// Rejects promise when http status is not ok
// Decodes and returns ok response in promise.
function simple_fetch(url,options){
    const responseType=options?.responseType ?? 'json';
    if(options?.responseType){delete options.responseType;}

    if(options?.get){
        for(const [key, value] of Object.entries(options.get)) {
            if(typeof value!=='number' && typeof value!=='string'){
                return Promise.reject('Error: simple_fetch: value for "'+key+'" is not of allowed type.');
            }
        }
        options.method='GET';
        url+=url.includes('?') ? '&' : '?';
        url+=(new URLSearchParams(options.get)).toString();
        delete options.get;
    }

    if(options?.post){
        options.method='POST';
        const data = new FormData();
        for(const [key, value] of Object.entries(options.post)) {
            if(typeof value!=='number' && typeof value!=='string'){
                return Promise.reject('Error: simple_fetch: value for "'+key+'" is not of allowed type.');
            }
            data.append(key,value);
        }
        delete options.post;
        options.body=data;
    }

    if(options?.postJson){
        options.method='POST';
        if(!options.headers){options.headers={};}
        options.headers['Accept']='application/json';
        options.headers['Content-Type']='application/json';
        options.body=JSON.stringify(options.postJson);
        delete options.postJson;
    }

    //console.log(url,options);
    return fetch(url,options).then(response=>{
        if(!response.ok){
            return response[responseType]().then((r)=>{
                return Promise.reject(r);
            });
        }
        return response[responseType]();
    });
}

function range_ta_chambre(nom,proba=.5){
    return new Promise((resolve,reject)=>{
        console.log(nom+': Je te promets de ranger ma chambre');
        if(Math.random()>=proba){
            setTimeout(()=>{
                const div=document.createElement('div');
                div.innerHTML='<img src="https://moodle.iutv.univ-paris13.fr/img/bjs2/extraterrestre.svg"/><br/><span class="nom"></span>';
                div.style.textAlign='center';
                div.style.position='absolute';
                div.style.left=(Math.random()*300)+'px';
                div.style.top=(Math.random()*150)+'px';;
                div.querySelector('img').style.width='200px';
                let snom= div.querySelector('.nom');
                snom.textContent=nom;
                snom.style.backgroundColor='green';
                snom.style.color='white';
                snom.style.padding='.5em';
                document.body.append(div);
                setTimeout(()=>{div.remove()},200);
                console.log(nom+": AÃ¯e ... un extraterrestre m'a lancÃ© un rayon paralysant et je n'ai pas pu ranger.");
                reject('DÃ©solÃ©');
            },3000);
        }
        else {
            setTimeout(()=>{
                console.log(nom+": C'est rangÃ©!");
                resolve('Ok!');
            },2000);
        }
    });
}

function attendre(d){
    return new Promise((resolve,reject)=>{
        setTimeout(()=>{
            resolve('Attende de '+d+'ms finie');
        },d);
    });
}


<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8"/>
    <title>RH employées</title>
    <link rel="stylesheet" href="rh_employee.css" />
    <script src="bjs2-js-lib.js"></script>
</head>
<body>
<h1>Site de RH employées</h1>



<script src="rh_employee.js"></script>
</body>
</html>


body{
    background: linear-gradient(45deg, rgba(255,255,255,1) 0%, rgba(149,222,237,1) 100%);
    background-repeat: no-repeat;
    padding: 2em;
    font-family: sans;
    color: #444;
    background-attachment: fixed;
}
Pour créer deux pages distinctes pour ajouter un employé et supprimer un employé, nous allons structurer le projet avec des fichiers HTML séparés pour chaque page et gérer les interactions via `rh_employee.js`. 

### Structure des fichiers

1. `index.html` - Page d'accueil ou de navigation
2. `add_employee.html` - Page pour ajouter un employé
3. `delete_employee.html` - Page pour supprimer un employé
4. `rh_employee.js` - Fichier JavaScript pour gérer les interactions

### Contenu des fichiers

#### Fichier: `index.html`

```html
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8"/>
    <title>RH employées</title>
    <link rel="stylesheet" href="rh_employee.css" />
</head>
<body>
    <h1>Site de RH employées</h1>
    <nav>
        <ul>
            <li><a href="add_employee.html">Ajouter un employé</a></li>
            <li><a href="delete_employee.html">Supprimer un employé</a></li>
        </ul>
    </nav>
</body>
</html>
```

#### Fichier: `add_employee.html`

```html
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8"/>
    <title>Ajouter un employé</title>
    <link rel="stylesheet" href="rh_employee.css" />
    <script src="bjs2-js-lib.js"></script>
</head>
<body>
    <h1>Ajouter un employé</h1>
    <form id="addEmployeeForm">
        <label for="name">Nom:</label>
        <input type="text" id="name" name="name" required>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
        <label for="position">Poste:</label>
        <input type="text" id="position" name="position" required>
        <button type="submit">Ajouter</button>
    </form>
    <script src="rh_employee.js"></script>
</body>
</html>
```

#### Fichier: `delete_employee.html`

```html
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8"/>
    <title>Supprimer un employé</title>
    <link rel="stylesheet" href="rh_employee.css" />
    <script src="bjs2-js-lib.js"></script>
</head>
<body>
    <h1>Supprimer un employé</h1>
    <form id="deleteEmployeeForm">
        <label for="employeeId">ID de l'employé:</label>
        <input type="text" id="employeeId" name="employeeId" required>
        <button type="submit">Supprimer</button>
    </form>
    <script src="rh_employee.js"></script>
</body>
</html>
```

#### Fichier: `rh_employee.js`

```javascript
document.addEventListener('DOMContentLoaded', () => {
    // Gestion du formulaire d'ajout d'employé
    const addEmployeeForm = document.getElementById('addEmployeeForm');
    if (addEmployeeForm) {
        addEmployeeForm.addEventListener('submit', (event) => {
            event.preventDefault();

            const name = addEmployeeForm.name.value;
            const email = addEmployeeForm.email.value;
            const position = addEmployeeForm.position.value;

            const newEmployee = {
                name: name,
                email: email,
                position: position
            };

            simple_fetch('http://your-api-url/employees', {
                method: 'POST',
                postJson: newEmployee,
                responseType: 'json'
            })
            .then(response => {
                console.log('Employee added:', response);
                addEmployeeForm.reset();
            })
            .catch(error => {
                console.error('There was an error adding the employee!', error);
            });
        });
    }

    // Gestion du formulaire de suppression d'employé
    const deleteEmployeeForm = document.getElementById('deleteEmployeeForm');
    if (deleteEmployeeForm) {
        deleteEmployeeForm.addEventListener('submit', (event) => {
            event.preventDefault();

            const employeeId = deleteEmployeeForm.employeeId.value;

            simple_fetch(`http://your-api-url/employees/${employeeId}`, {
                method: 'DELETE',
                responseType: 'json'
            })
            .then(response => {
                console.log('Employee deleted:', response);
                deleteEmployeeForm.reset();
            })
            .catch(error => {
                console.error('There was an error deleting the employee!', error);
            });
        });
    }
});
```

### Résumé

Avec ces fichiers, vous avez deux pages distinctes (`add_employee.html` et `delete_employee.html`) pour ajouter et supprimer des employés. Le fichier JavaScript `rh_employee.js` gère les interactions pour les deux pages en utilisant la fonction `simple_fetch` définie dans `bjs2-js-lib.js`. Assurez-vous de remplacer `http://your-api-url/employees` par l'URL réelle de votre API REST.
