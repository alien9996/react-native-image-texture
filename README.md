# react-native-image-texture

**Libraries add mode lighting effects to your images**

## Demo

![gif](https://github.com/alien9996/library-gif/blob/main/Texture.gif?raw=true)

## Getting started

`$ npm install react-native-image-texture --save`

<br />
Or
<br />
`$ yarn add react-native-image-texture`

### Mostly automatic installation (react-native < 0.6)

`$ react-native link react-native-image-texture`

### Manual installation (react-native < 0.6)

#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-image-texture` and add `RNImageTexture.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNImageTexture.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`

- Add `import com.reactimagetexture.RNImageTexturePackage;` to the imports at the top of the file
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

## Example

### You have two choices to use the library.

1. **Resource use is available.**

```javascript
import RNImageTexture from "react-native-image-texture";

RNImageTexture.getResourcesImageTexture(
  {
    imageSource1: "/storage/emulated/0/Download/img.jpg",
    imageSource2: null,
    dataType1: "Path",
    dataType2: "Path",
    textureType: 3,
    isAccsets: true,
  },
  (source) => {
    this.setState((imgBase64: source.base64));
    console.log("SOURCE", source);
    // "source" returns the height, width and the Base64 string of the image.
  }
);
```

**The result you get will be the same as the demo**

2. **Use an external rescource of your**

```javascript
import RNImageTexture from "react-native-image-texture";

RNImageTexture.getResourcesImageTexture(
  {
    imageSource1: "/storage/emulated/0/Download/img.jpg",
    imageSource2: "/storage/emulated/0/Download/img2.jpg",
    dataType1: "Path",
    dataType2: "Path",
    textureType: 0,
    isAccsets: false,
    mode: 2,
  },
  (source) => {
    this.setState((imgBase64: source.base64));
    console.log("SOURCE", source);
    // "source" returns the height, width and the Base64 string of the image.
  }
);
```

**You will get the following result**

![Demo1](https://github.com/alien9996/library-gif/blob/main/demo.png?raw=true)

## Options

| Props                 | Default | Options/Info                                                                                                                                                           |
| --------------------- | ------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| imageSource1 (String) | null    | The path to the image in the device or a Base64 string.                                                                                                                |
| imageSource2 (String) | null    | The path to the image in the device or a Base64 string.                                                                                                                |
| dataType1 (String)    | Path    | If you send a path, enter the string "Path"<br>If you send a Base64 string, enter the string "Base64".                                                                 |
| dataType2 (String)    | Path    | If you send a path, enter the string "Path"<br>If you send a Base64 string, enter the string "Base64".<br> **Note**: Valid only when isAccsets = false.                |
| textureType (int)     | 0       | Select the type you want to process images, the values from 0 to 22. Other values around 0 to 22 will not take effect.<br> **Note**: Valid only when isAccsets = true. |
| isAccsets (boolean)   | true    | If you want use the resource, select **true**.<br>If you do not want use resource, select **false**.                                                                   |
| mode (int)            | 3       | Select light mode, with values of 1, 2 and 3.                                                                                                                          |

## Texture types

![filterType](https://github.com/alien9996/library-gif/blob/main/texture_type.png?raw=true)

## Note

- The image path you send into **imageSource1:''** and **imageSource2:''** must be the absolute path. If you have problems with the absolute path, you can find the solution [here](https://stackoverflow.com/questions/52423067/how-to-get-absolute-path-of-a-file-in-react-native).

### Thank you for your interest!
