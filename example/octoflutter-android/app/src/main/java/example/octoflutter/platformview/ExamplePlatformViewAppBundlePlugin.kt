/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, The OctoFlutter Authors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package example.octoflutter.platformview

import cn.xiaochuankeji.octoflutter.embedding.android.FlutterActivityAndFragmentDelegate
import cn.xiaochuankeji.octoflutter.embedding.android.FlutterActivityAndFragmentDelegate.Host
import cn.xiaochuankeji.octoflutter.embedding.engine.plugins.FlutterPlugin
import cn.xiaochuankeji.octoflutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import cn.xiaochuankeji.octoflutter.isolate.AppBundlePlugin

/**
 * Created by liujian on 2023/3/13.
 */
class ExamplePlatformViewAppBundlePlugin : AppBundlePlugin {

    private val bundleMap: MutableMap<Int, ExamplePlatformViewChannel> = mutableMapOf()

    override fun onAttachAppBundle(
        bid: Int,
        binding: FlutterPlugin.FlutterPluginBinding?,
        host: FlutterActivityAndFragmentDelegate.Host?
    ) {
        bundleMap[bid] = ExamplePlatformViewChannel(bid, binding!!, host)
    }

    override fun onDetachAppBundle(
        bid: Int,
        binding: FlutterPlugin.FlutterPluginBinding?,
        host: FlutterActivityAndFragmentDelegate.Host?
    ) {
        if (bundleMap.containsKey(bid)) {
            bundleMap.remove(bid)?.destroy()
        }
    }


    private class ExamplePlatformViewChannel(
        private val bid: Int,
        private val binding: FlutterPluginBinding,
        private var host: Host?
    ) {
        init {
            binding.platformViewRegistry().registerAppBundleViewFactory(
                bid, "my_platform_view", ExamplePlatformViewFactory(
                    bid,
                    binding.binaryMessenger
                )
            )
        }

        fun destroy() {
            binding.platformViewRegistry().unRegisterAppBundleViewFactory(bid, "my_platform_view")
            host = null
        }
    }

}