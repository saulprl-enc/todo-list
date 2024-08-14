# Breakable Toy - ToDo List

Welcome to my Breakable Toy's repository. In this README, you'll find all the necessary steps to run the project.

## Running the project

1. Clone this repository.

Before doing anything else, you must clone this repository, which can be done by running the following command:

```bash
git clone git@github.com:saulprl-enc/todo-list.git
```

2. Installing dependencies.

The front-end app needs its dependencies to be installed before attempting to run any scripts. First, navigate to the front-end project using `cd todolist-app` and then running:

```bash
npm install
```

As a side note, this project was created using `Node.js v20.15.1`.

3. Running the project.

**Recommendation: prepare at least to terminals in order to execute each project separately**. Otherwise, you can run one project, send it to the background (with `Ctrl + Z`), and execute the other one.

- Back-end.

First, navigate to the back-end's directory and then run `mvn spring-boot:run`:

```bash
cd todolistbackend
mvn spring-boot:run
```

This will install the necessary dependencies, compile the code, and ultimately run the project on port `9090`.

- Front-end.

First, navigate to the front-end's directory and then run `npm start` or `npm run start`:

```bash
cd todolist-app
npm run start # or npm start
```

The command line will notify you when the project's up and running on port `8080`, and will give you the URL.

4. Testing the project.

This project contains several tests for both the back-end and the front-end. Here's how to run them:

- Back-end

First, navigate to the back-end's directory, and then run the command `mvn test`:

```bash
cd todolistbackend
mvn test
```

- Front-end

Navigate to the front-end's directory, and then run `npm run test`:

```bash
cd todolist-app
npm run test
```
