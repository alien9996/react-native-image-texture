using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Image.Texture.RNImageTexture
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNImageTextureModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNImageTextureModule"/>.
        /// </summary>
        internal RNImageTextureModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNImageTexture";
            }
        }
    }
}
