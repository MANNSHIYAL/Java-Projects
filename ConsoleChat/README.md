## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Hashcode format
- `Peer`: Peer1@Peer2
- `Chat`: ChatRoom@Creater

## Special Character
`$&$`: Trying to use it to seperate key-value pairs.


- This current architecture is not the best architecture it's just a prototype. However a better architecture will use a ConcurrentQueue based messaging system where there will be a queue for each connected user.
- Peer-to-peer chat is working fine, however, the project needs a lot of improvements. The chat functionality is still pending but it won't be that difficult to implement as I have already implemented the basis peer-to-peer the rest will be similar to the other project that I have made.
