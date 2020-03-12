package com.tju.myproject.controller;

import com.tju.myproject.dao.ArticleRepository;
import com.tju.myproject.entity.Article;
import com.tju.myproject.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.jvm.hotspot.debugger.Page;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TestController
{
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private Tools tools;
    @GetMapping("/api/test")
    public Object teststr()
    {
        String str="asdasf###asfd";
        System.out.println(tools.removeMarkdown(str));
       return null;
    }
    @GetMapping("/api/testa")
    public Object test(String str)
    {
        Article article=new Article();
        article.setDomain(2);
        article.setTopic(13);
        article.setTitle("迁移学习简介");
        article.setContent("迁移学习是机器学习方法之一，它可以把为一个任务开发的模型重新用在另一个不同的任务中，并作为另一个任务模型的起点。\n" +
                "\n" +
                "这在深度学习中是一种常见的方法。由于在计算机视觉和自然语言处理上，开发神经网络模型需要大量的计算和时间资源，技术跨度也比较大。所以，预训练的模型通常会被重新用作计算机视觉和自然语言处理任务的起点。\n" +
                "\n" +
                "这篇文章会发现告诉你，如何使用迁移学习来加速训练过程和提高深度学习模型的性能，以及解答以下三个问题：\n" +
                "\n" +
                "1.\n" +
                "什么是迁移学习，以及如何使用它\n" +
                "2.\n" +
                "深度学习中迁移学习的常见例子\n" +
                "3.\n" +
                "在自己的预测模型问题上什么时候使用迁移学习\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "![](https://img-blog.csdn.net/20180106134955050?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZFFDRkt5UURYWW0zRjhyQjA=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)<br>\n" +
                "\n" +
                "深入学习中关于迁移学习的简要介绍Mike拍摄的关于鸟的照片<br>\n" +
                "\n" +
                "**什么是迁移学习？**\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "迁移学习是机器学习技术的一种，其中在一个任务上训练的模型被重新利用在另一个相关的任务上。\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "书本解释：\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "\"迁移学习和领域自适应指的是将一个任务环境中学到的东西用来提升在另一个任务环境中模型的泛化能力\"  ——2016年\"Deep Learning\"，526页\n" +
                "\n" +
                "迁移学习也是一种优化方法，可以在对另一个任务建模时提高进展速度或者是模型性能。\n" +
                "\n" +
                "\"迁移学习就是通过从已学习的相关任务中迁移其知识来对需要学习的新任务进行提高。\"\n" +
                "\n" +
                "——第11章：转移学习，机器学习应用研究手册，2009年。\n" +
                "\n" +
                "迁移学习还与多任务学习和概念漂移等问题有关，它并不完全是深度学习的一个研究领域。\n" +
                "\n" +
                "尽管如此，由于训练深度学习模型所需耗费巨大资源，包括大量的数据集，迁移学习便成了深度学习是一种很受欢迎的方法。但是，只有当从第一个任务中学到的模型特征是容易泛化的时候，迁移学习才能在深度学习中起到作用。\n" +
                "\n" +
                "\"在迁移学习中，我们首先在基础数据集和任务上训练一个基础网络，然后将学习到的特征重新调整或者迁移到另一个目标网络上，用来训练目标任务的数据集。如果这些特征是容易泛化的，且同时适用于基本任务和目标任务，而不只是特定于基本任务，那迁移学习就能有效进行。\"     ——深度神经网络中的特征如何迁移的？\n" +
                "\n" +
                "这种用于深度学习的迁移学习形式被称为推导迁移（inductive transfer）。就是通过使用合适但不完全相同的相关任务的模型，将模型的范围（模型偏差）以有利的方式缩小。\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "![](https://img-blog.csdn.net/20180106135021659?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZFFDRkt5UURYWW0zRjhyQjA=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)<br>\n" +
                "\n" +
                "推导迁移的描述 采取\"迁移学习\"\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "**如何使用迁移学习？**\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "你可以在你自己的预测模型问题上使用迁移学习。通常有两种方法：\n" +
                "\n" +
                "*\n" +
                "开发模型方法\n" +
                "*\n" +
                "预训练模型方法\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "开发模型方法\n" +
                "\n" +
                "1.\n" +
                "选择源任务。你必须选择一个与大量数据相关的预测模型问题，这个大量的数据需要与输入数据，输出数据和/或从输入到输出数据映射过程中学习的概念之间存在某种关系。\n" +
                "2.\n" +
                "开发源模型。接下来，你必须为这个第一项任务开发一个熟练的模型。该模型必须比原始模型更好，以确保一些特征学习已经发挥了其作用。\n" +
                "3.\n" +
                "重用模型。然后可以将适合元任务的模型用作感兴趣的另一个任务模型的起点。这取决于所使用的建模技术，可能涉及到了全部或部分模型。\n" +
                "4.\n" +
                "调整模型。可选项，对感兴趣任务的调整输入—输出配对数据或改进模型。\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "预训练模型方法\n" +
                "\n" +
                "1.\n" +
                "选择源任务。从可用的模型中选择预训练的元模型。许多研究机构会发布已经在大量的且具有挑战性的数据集上训练好的模型，在可用模型的模型池里面也能找到这些模型。\n" +
                "2.\n" +
                "重用模型。然后可以将预训练的模型用作感兴趣的另一个任务模型的起点。这取决于所使用的建模技术，可能涉及使用全部或部分模型。\n" +
                "3.\n" +
                "调整模型。可选项，对感兴趣任务的调整输入—输出配对数据或改进模型。\n" +
                "\n" +
                "第二类迁移学习方法在深度学习领域是很常见的。\n" +
                "\n" +
                "**深度学习中迁移学习的例子**\n" +
                "\n" +
                "让我们用深度学习模型中两个常见的例子来具体说明迁移学习。\n" +
                "\n" +
                "迁移学习与图像数据\n" +
                "\n" +
                "使用图像数据作为输入的预测模型问题中进行迁移学习是很常见的。\n" +
                "\n" +
                "它可能是一个以照片或视频数据作为输入的预测任务。\n" +
                "\n" +
                "对于这些类型的问题，通常会使用预先训练好的深度学习模型来处理大型的和具有挑战性的图像分类任务，例如ImageNet 1000级照片分类竞赛。\n" +
                "\n" +
                "为此次竞赛开发模型的研究机构通常会发布最终的模型，并在许可条例下允许重复使用。 而训练这些模型可能需要在现代的硬件上花费几天或者几周。\n" +
                "\n" +
                "可以直接下载这些模型，并将其合并到以自己图像数据作为输入的新模型中。\n" +
                "\n" +
                "这类型模型的三个例子包括：\n" +
                "\n" +
                "*\n" +
                "牛津的VGG模型\n" +
                "*\n" +
                "谷歌的Inception模型\n" +
                "*\n" +
                "微软的ResNet模型\n" +
                "\n" +
                "有关更多示例，请参阅 Caffe Model Zoo ，其中共享了更多预先训练的模型。\n" +
                "\n" +
                "Caffe Model Zoo地址：https://github.com/BVLC/caffe/wiki/Model-Zoo\n" +
                "\n" +
                "这种方法是有效的，因为图像是从大量的照片上选出来进行训练的，并且要求模型对相对较多的类进行预测，反过来要求模型能够有效地从照片中提取到特征以便在具体问题上有好的效果。\n" +
                "\n" +
                "在斯坦福大学关于视觉识别的卷积神经网络课程中，作者谨慎地选择了在新模型中能够使用的预训练模型。\n" +
                "\n" +
                "\"[卷积神经网络]特征在早期层中更为通用，而后面的层更具有原始数据集特有的特征\"\n" +
                "\n" +
                "——迁移学习，CS231n卷积神经网络的视觉识别课程\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "迁移学习与语言数据\n" +
                "\n" +
                "使用文本作为输入或输出的自然语言处理问题进行迁移学习是很常见的。\n" +
                "\n" +
                "对于这些类型的问题，使用单词嵌入，即将单词映射到高维连续矢量空间，因为在这个矢量空间中相似含义的不同单词具有相似的矢量表示。\n" +
                "\n" +
                "存在一些有效的算法来学习这些分布式的文字表示，而且研究机构通常会在许可条例下发布预先训练的模型，这些模型是在非常大的文本文档上训练出来的。\n" +
                "\n" +
                "这种类型的两个例子包括：\n" +
                "\n" +
                "*\n" +
                "谷歌的word2vec模型\n" +
                "*\n" +
                "斯坦福的GloVe模型\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "可以下载这些分布式单词表示的模型并将其合并到深度学习语言模型中，以输入单词的解释或者作为从模型输出单词的生成这两种形式。\n" +
                "\n" +
                "Yoav Goldberg在他的深度学习之自然语言处理一书中提醒到：\n" +
                "\n" +
                "\"人们可以下载在大量具有差异的文本上训练过的预训练单词向量，并且基础语料库对该结果表示有非常大的影响，那么这个可用的预训练的表示可能不是您特定用例的最佳选择。\"\n" +
                "\n" +
                "—— 第135页，自然语言处理中的神经网络方法，2017。\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "**何时使用转移学习？**\n" +
                "\n" +
                "迁移学习是一种优化方法，是节省时间或获得更好性能的捷径。\n" +
                "\n" +
                "一般来说，要到模型开发和评估之后，才能体现迁移学习在某领域中使用的好处。\n" +
                "\n" +
                "Lisa Torrey和Jude Shavlik在迁移学习的章节中描述了使用迁移学习时要注意的三个可能的好处：\n" +
                "\n" +
                "*\n" +
                "更高的起点：源模型中的初始性能（在调节模型之前）比其他方法要高。\n" +
                "*\n" +
                "更大的坡度：在训练源模型期间性能的提高速度比其他情况下更陡峭。\n" +
                "*\n" +
                "更高的渐近线：训练好的模型的融合性能要好于其他情况。\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "![](https://img-blog.csdn.net/20180106135100989?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZFFDRkt5UURYWW0zRjhyQjA=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)<br>\n" +
                "\n" +
                "迁移可能改善学习的三种方式。来自\"Transfer\n" +
                " Learning\"\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "理想的情况下，你就会看到成功应用迁移学习的三个好处。\n" +
                "\n" +
                "通常，如果你能够用丰富的数据来完成相关的任务，并且你有资源为该任务开发一个模型并将其重用于你自己的问题，或者可以使用预训练的模型来作为你自己的模型的起点，那么你就可以试一试迁移学习。\n" +
                "\n" +
                "在一些你可能没有太多数据的问题上，迁移学习可以解锁一些你的技能。\n" +
                "\n" +
                "源数据或源模型的选择是一个公开的问题，这个选择可能需要通过专业领域知识和/或直觉的经验。\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "**总结**\n" +
                "\n" +
                "在这篇文章中，您了解了如何使用迁移学习来加速训练并提高深度学习模型的性能。\n" +
                "\n" +
                "具体来说，你了解到：\n" +
                "\n" +
                "1.\n" +
                "什么是迁移学习，以及如何在深度学习中使用。\n" +
                "2.\n" +
                "何时使用迁移学习。\n" +
                "3.\n" +
                "用于计算机视觉和自然语言处理任务中迁移学习的例子。");
        return articleRepository.save(article);
    }
}
