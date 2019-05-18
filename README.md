
# react-native-image-texture

## Getting started

`$ npm install react-native-image-texture --save`

### Mostly automatic installation

`$ react-native link react-native-image-texture`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-image-texture` and add `RNImageTexture.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNImageTexture.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNImageTexturePackage;` to the imports at the top of the file
  - Add `new RNImageTexturePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-image-texture'
  	project(':react-native-image-texture').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-image-texture/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-image-texture')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNImageTexture.sln` in `node_modules/react-native-image-texture/windows/RNImageTexture.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Image.Texture.RNImageTexture;` to the usings at the top of the file
  - Add `new RNImageTexturePackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNImageTexture from 'react-native-image-texture';

// TODO: What to do with the module?
RNImageTexture;
```
  