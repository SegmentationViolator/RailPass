# RailPass

A web-application for academic institutes, and students to approve, and appeal for railway pass concessions

Created by Ananya Madankar, Jash Mistry, Mahesh Patil, and Saad Kondvilkar, in the academic year 2025-2026 as a mini-project
for the Full Stack Java Programming course.

## Usage

Clone the repository, and cd into it

```
git clone --recurse-submodules https://github.com/SegmentationViolator/RailPass.git
cd RailPass
```

Build the frontend

```
cd frontend
npm i
npm run build
```

Go back to the parent directory

```
cd ..
```

Start a MySQL server, create a MySQL database named "railpass", keep the server running

Put your MySQL credentials (username and password) in src/main/resources/application.properties

Set TOKEN_SECRET environment variable to a Base64 encoded string (containing at least 32--ideally random--bytes)

Execute the following command,

On Linux/Unix:

```sh
./mvnw spring-boot:run
```

On Windows:

```sh
.\mvnw.cmd spring-boot:run
```

After the first run, change

```properties
spring.jpa.hibernate.ddl-auto=create
```

to

```properties
spring.jpa.hibernate.ddl-auto=validate
```

in src/main/resources/application.properties
